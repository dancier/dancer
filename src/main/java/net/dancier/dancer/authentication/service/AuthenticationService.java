package net.dancier.dancer.authentication.service;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import net.dancier.dancer.authentication.UserOrEmailAlreadyExistsException;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.*;
import net.dancier.dancer.authentication.repository.*;
import net.dancier.dancer.core.exception.AppliationException;
import net.dancier.dancer.core.exception.BusinessException;
import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.mail.service.MailCreationService;
import net.dancier.dancer.mail.service.MailEnqueueService;
import net.dancier.dancer.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    public static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailValidationCodeRepository emailValidationCodeRepository;

    private final PasswordResetCodeRepository passwordResetCodeRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final MailEnqueueService mailEnqueueService;

    private final MailCreationService mailCreationService;

    private final VerifiedActionCodeRepository verifiedActionCodeRepository;
    private final String frontendBaseName;

    public Authentication authenticate(Authentication authentication) {
        return this.authenticationManager.authenticate(authentication);
    }
    public String generateJwtToken(Authentication authentication) {
        return this.tokenProvider.generateJwtToken(authentication);
    }

    public String generateJwtToken(String subject) {
        return this.tokenProvider.generateJwtToken(subject);
    }

    public Cookie generateCookie(String token) {
        Cookie cookie = new Cookie("jwt-token", token);
        // one month
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public User getUser(UUID userId) {
        try {
            return this.userRepository.getById(userId);
        } catch (EntityNotFoundException entityNotFoundException) {
            throw new NotFoundException("No user found with this userId: " + userId, entityNotFoundException);
        }
    }
    @Transactional
    public void registerUser(RegisterRequestDto signUpRequest) {
        log.info("Attempting to register user: " + signUpRequest.getEmail());

        if(userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            log.info("User or email already exists.");
            handleRegistrationAttemptOfAlreadyExistingAccount(userRepository.findByEmail(signUpRequest.getEmail()).get());
        } else {
            User user = new User(
                    signUpRequest.getEmail(),
                    signUpRequest.getPassword());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppliationException("User Role could not be set."));
            Role humanRole = roleRepository.findByName(RoleName.ROLE_HUMAN)
                    .orElseThrow(() -> new AppliationException("Human Role could not be set."));
            List<Role> roles = new ArrayList<>();
            roles.add(userRole);
            roles.add(humanRole);
            user.setRoles(roles);

            User savedUser = userRepository.save(user);
            userRepository.flush();
            createEmailValidationCode(savedUser);
        }
    }

    private void handleRegistrationAttemptOfAlreadyExistingAccount(User user) {
        String passwordResetCode = createPasswordResetCode(user.getEmail());
        enqueueTypedUserMail(user,"Gibt es schon", MailCreationService.USER_ALREADY_EXISTS_EMAIL,
                Map.of("passwordResetLink",
                        frontendBaseName + "/authentication/reset-password/" + passwordResetCode,
                        "email", user.getEmail(),
                        "loginLink", frontendBaseName + "/login")
                );
    }

    @Transactional
    public void createEmailValidationCode(User user) {
        Objects.requireNonNull(user.getId());
        EmailValidationCode emailValidationCode = emailValidationCodeRepository
                .findByUserId(user.getId())
                .orElseGet(() -> new EmailValidationCode());
        emailValidationCode.setExpiresAt(Instant
                .now()
                .plus(3, ChronoUnit.HOURS));
        emailValidationCode.setUserId(user.getId());
        emailValidationCode.setCode(UUID.randomUUID().toString());
        emailValidationCodeRepository.save(emailValidationCode);
        enqueueUserMail(user, emailValidationCode.getCode());
        log.debug("Created validation code: " + emailValidationCode.getCode() + " for user: " + user);
    }

    public void createEmailValidationCode(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new AppliationException(""));
        createEmailValidationCode(user);
    }

    @Transactional
    public User checkEmailValidationCode(String code) {
        EmailValidationCode emailValidationCode = emailValidationCodeRepository
                .findByCode(code).orElseThrow(() ->new AppliationException("Unable to validate"));
        if (emailValidationCode.getExpiresAt().isBefore(Instant.now())) {
            throw new AppliationException("Unable to Validate, code already expired");
        };
        User user = userRepository.findById(emailValidationCode
                .getUserId())
                .orElseThrow(() -> new AppliationException("No user associated with this code."));
        user.setEmailValidated(true);
        emailValidationCodeRepository.delete(emailValidationCode);
        userRepository.save(user);
        userRepository.flush();
        return user;
    }

    public String createPasswordResetCode(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppliationException(""));
        PasswordResetCode passwordResetCode = passwordResetCodeRepository.findById(user.getId()).orElseGet(() -> new PasswordResetCode());
        passwordResetCode.setExpiresAt(Instant.now().plus(3, ChronoUnit.HOURS));
        passwordResetCode.setUserId(user.getId());
        passwordResetCode.setCode(UUID.randomUUID().toString());
        passwordResetCodeRepository.save(passwordResetCode);
        log.debug("Create password code: " + passwordResetCode.getCode());
        return passwordResetCode.getCode();
    }

    public String checkPasswortCodeRequestAndCreateNew(String code) {
        PasswordResetCode passwordResetCode = this.passwordResetCodeRepository
                .findByCode(code)
                .orElseThrow(
                        () -> new BusinessException("No such code"));
        RandomString randomString = new RandomString();
        String newPassword = randomString.nextString();
        User user = userRepository.getById(passwordResetCode.getUserId());
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetCodeRepository.delete(passwordResetCode);
        log.info(passwordResetCode.toString());
        return newPassword;
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    private void enqueueTypedUserMail(User user,
                                      String subject,
                                      String template,
                                      Map<String, Object> data) {
        mailEnqueueService.enqueueMail(
                mailCreationService.createDancierMessageFromTemplate(
                        user.getEmail(),
                        MailCreationService.NO_REPLY_FROM,
                        subject,
                        template,
                        data
                )
        );
    }
    private void enqueueUserMail(User user, String validationCode) {
        mailEnqueueService.enqueueMail(
                mailCreationService.createDancierMessageFromTemplate(
                        user.getEmail(),
                        MailCreationService.NO_REPLY_FROM,
                        "Dancier - bestätige Deine E-Mail-Adresse!",
                        MailCreationService.NEW_USER_VALIDATE_EMAIL,
                        Map.of( "validationLink", frontendBaseName + "/registration/verify-account/" + validationCode)
                ));
    }

    private VerifiedActionCode createNewVerifiedActionCode(VerifiedActionCode.Action action, UUID userId) {
        VerifiedActionCode verifiedActionCode = new VerifiedActionCode();
        verifiedActionCode.setExpiresAt(Instant.now().plus(3, ChronoUnit.HOURS));
        verifiedActionCode.setUserId(userId);
        verifiedActionCode.setCode(UUID.randomUUID().toString());
        verifiedActionCode.setAction(action);
        return verifiedActionCode;
    }
}

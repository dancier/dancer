package net.dancier.dancer.authentication.service;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.event.NewUserCreatedEvent;
import net.dancier.dancer.authentication.model.*;
import net.dancier.dancer.authentication.repository.*;
import net.dancier.dancer.core.config.CookieConfiguration;
import net.dancier.dancer.core.exception.ApplicationException;
import net.dancier.dancer.core.exception.BusinessException;
import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.mail.service.MailCreationService;
import net.dancier.dancer.mail.service.MailEnqueueService;
import net.dancier.dancer.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.Duration;
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
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CookieConfiguration cookieConfiguration;

    public Authentication authenticate(Authentication authentication) {
        return this.authenticationManager.authenticate(authentication);
    }
    public String generateJwtToken(Authentication authentication) {
        return this.tokenProvider.generateJwtToken(authentication);
    }

    public String generateJwtToken(String subject) {
        return this.tokenProvider.generateJwtToken(subject);
    }

    public ResponseCookie generateCookie(String token) {
        return ResponseCookie.from("jwt-token", token)
                .maxAge(Duration.ofDays(30))
                .secure(cookieConfiguration.getSecure())
                .httpOnly(true)
                .path("/")
                .sameSite(cookieConfiguration.getSameSite())
                .build();
    }

    /**
     * Generates a cookie that clears the jwt-token cookie.
     * By setting the maxAge to 0, the cookie will be deleted by the browser.
     */
    public ResponseCookie generateClearingCookie() {
        return ResponseCookie.from("jwt-token", "")
                              .build();
    }

    public User getUser(UUID userId) {
        try {
            return this.userRepository.getReferenceById(userId);
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
                    .orElseThrow(() -> new ApplicationException("User Role could not be set."));
            Role humanRole = roleRepository.findByName(RoleName.ROLE_HUMAN)
                    .orElseThrow(() -> new ApplicationException("Human Role could not be set."));
            List<Role> roles = new ArrayList<>();
            roles.add(userRole);
            roles.add(humanRole);
            user.setRoles(roles);

            User savedUser = userRepository.save(user);
            userRepository.flush();
            applicationEventPublisher.publishEvent(
                    NewUserCreatedEvent.builder()
                            .id(savedUser.getId())
                            .email(savedUser.getEmail())
                            .isEmailValidated(savedUser.isEmailValidated()).build()
            );
        }
    }

    private void handleRegistrationAttemptOfAlreadyExistingAccount(User user) {
        String passwordResetCode = createPasswordResetCode(user);
        String loginLink =
                user.isEmailValidated()
                        ?
                            loginLink():
                            emailValidationLink(createEmailValidationCode(user));

        enqueueTypedUserMail(user.getEmail(),"Du bist schon Mitglied bei dancier.net ;-)", MailCreationService.USER_ALREADY_EXISTS_EMAIL,
                Map.of("passwordResetLink", passwordResetLink(passwordResetCode),
                        "email", user.getEmail(),
                        "loginLink", loginLink)
                );
    }

    private String passwordResetLink(String passwordResetCode) {
        return frontendBaseName + "/registration/reset-password/" + passwordResetCode;
    }

    private String emailValidationLink(String validationCode) {
        return frontendBaseName + "/registration/verify-account/" + validationCode;
    }
    private String loginLink() {
        return frontendBaseName + "/login";
    }
    @Transactional
    public String createEmailValidationCode(User user) {
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
        return emailValidationCode.getCode();
    }

    public void createEmailValidationCode(String email) {
        userRepository
                .findByEmail(email)
                .ifPresent (u->createEmailValidationCode(u));
    }

    @Transactional
    public User checkEmailValidationCode(String code) {
        EmailValidationCode emailValidationCode = emailValidationCodeRepository
                .findByCode(code).orElseThrow(() ->new ApplicationException("Unable to validate"));
        if (emailValidationCode.getExpiresAt().isBefore(Instant.now())) {
            throw new ApplicationException("Unable to Validate, code already expired");
        }
        User user = userRepository.findById(emailValidationCode
                .getUserId())
                .orElseThrow(() -> new ApplicationException("No user associated with this code."));
        user.setEmailValidated(true);
        emailValidationCodeRepository.delete(emailValidationCode);
        userRepository.save(user);
        userRepository.flush();
        return user;
    }

    public void valideEmailByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email not exist"));
        user.setEmailValidated(true);
        userRepository.save(user);
    }

    public Optional<String> createPasswordResetCode(String email) {
        return userRepository.findByEmail(email).map(u -> createPasswordResetCode(u));
    }

    public String createPasswordResetCode(User user) {
        PasswordResetCode passwordResetCode = passwordResetCodeRepository.findById(user.getId()).orElseGet(() -> new PasswordResetCode());
        passwordResetCode.setExpiresAt(Instant.now().plus(3, ChronoUnit.HOURS));
        passwordResetCode.setUserId(user.getId());
        passwordResetCode.setCode(UUID.randomUUID().toString());
        passwordResetCodeRepository.save(passwordResetCode);
        log.debug("Create password code: " + passwordResetCode.getCode());
        return passwordResetCode.getCode();
    }

    public void checkPasswortCodeRequestAndCreateNew(String code, String newPassword) {
        PasswordResetCode passwordResetCode = this.passwordResetCodeRepository
                .findByCode(code)
                .orElseThrow(
                        () -> new BusinessException("No such code"));
        User user = userRepository.getReferenceById(passwordResetCode.getUserId());
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetCodeRepository.delete(passwordResetCode);
        log.info(passwordResetCode.toString());
    }

    public void sendChangePasswordMail(String email, String code) {
        enqueueTypedUserMail(email,
                "Du möchtest dein Passwort auf dancier.net ändern...",
                MailCreationService.PASSWORD_CHANGE_REQUEST_EMAIL,
                Map.of("changePasswordLink", passwordResetLink(code))
        );

    }    private void enqueueTypedUserMail(String email,
                                      String subject,
                                      String template,
                                      Map<String, Object> data) {
        mailEnqueueService.enqueueMail(
                mailCreationService.createDancierMessageFromTemplate(
                        email,
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
                    Map.of( "validationLink", emailValidationLink(validationCode)
                ))
        );
    }

}

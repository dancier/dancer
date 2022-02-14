package net.dancier.dancer.authentication.service;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import net.dancier.dancer.authentication.UserOrEmailAlreadyExistsException;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.*;
import net.dancier.dancer.authentication.repository.EmailValidationCodeRepository;
import net.dancier.dancer.authentication.repository.PasswordResetCodeRepository;
import net.dancier.dancer.authentication.repository.RoleRepository;
import net.dancier.dancer.authentication.repository.UserRepository;
import net.dancier.dancer.core.exception.AppliationException;
import net.dancier.dancer.core.exception.BusinessException;
import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

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

    public Authentication authenticate(Authentication authentication) {
        return this.authenticationManager.authenticate(authentication);
    }
    public String generateToken(Authentication authentication) {
        return this.tokenProvider.generateToken(authentication);
    }

    public User getUser(UUID userId) {
        try {
            return this.userRepository.getById(userId);
        } catch (EntityNotFoundException entityNotFoundException) {
            throw new NotFoundException("No user found with this userId: " + userId, entityNotFoundException);
        }
    }

    public User registerUser(RegisterRequestDto signUpRequest) {
        log.info("Attempting to register user: " + signUpRequest.getEmail());
        if(userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            log.info("User or email already exists.");
            throw new UserOrEmailAlreadyExistsException("User: " + signUpRequest.getEmail() + " already exists.");
        }
        User user = new User(
                signUpRequest.getEmail(),
                signUpRequest.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppliationException("User Role could not be set."));
        user.setRoles(Collections.singleton(userRole));

        User savedUser = userRepository.save(user);
        EmailValidationCode emailValidationCode = new EmailValidationCode();
        emailValidationCode.setExpiresAt(Instant.now().plus(3, ChronoUnit.HOURS));
        emailValidationCode.setUserId(savedUser.getId());
        emailValidationCode.setCode(UUID.randomUUID().toString());
        log.debug("Validation code: " + emailValidationCode.getCode());
        emailValidationCodeRepository.save(emailValidationCode);
        return savedUser;
    }

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
        log.debug("Created validation code: " + emailValidationCode.getCode() + " for user: " + user);
    }

    public void createEmailValidationCode(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new AppliationException(""));
        createEmailValidationCode(user);
    }

    @Transactional
    public void checkEmailValidationCode(String code) {
        EmailValidationCode emailValidationCode = emailValidationCodeRepository
                .findByCode(code).orElseThrow(() ->new AppliationException("Unable to validate"));
        if (emailValidationCode.getExpiresAt().isBefore(Instant.now())) {
            throw new AppliationException("Unable to Validate");
        };
        if (!emailValidationCode.getCode().contentEquals(code)) {
            throw new AppliationException("unable to validate");
        }
        User user = userRepository.findById(emailValidationCode.getUserId()).orElseThrow(() -> new AppliationException(""));
        user.setEmailValidated(true);
        emailValidationCodeRepository.delete(emailValidationCode);
        userRepository.save(user);
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
}

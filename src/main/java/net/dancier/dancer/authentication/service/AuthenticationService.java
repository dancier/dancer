package net.dancier.dancer.authentication.service;

import net.dancier.dancer.authentication.UserOrEmailAlreadyExistsException;
import net.dancier.dancer.authentication.repository.UserRepository;
import net.dancier.dancer.authentication.repository.ValidationCodeRepository;
import net.dancier.dancer.authentication.model.*;
import net.dancier.dancer.controller.payload.SignUpRequest;
import net.dancier.dancer.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;

@Service
public class AuthenticationService {

    public static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ValidationCodeRepository validationCodeRepository;

    public User getUser(UUID userId) {
        return this.userRepository.getById(userId);
    }

    public User registerUser(SignUpRequest signUpRequest) {
        log. info("Checking for existing user: " + signUpRequest.getUsername());
        if(userRepository.findByUsernameOrEmail(signUpRequest.getUsername(), signUpRequest.getEmail()).isPresent()) {
            log.info("User or email already exists.");
            throw new UserOrEmailAlreadyExistsException("User: " + signUpRequest.getUsername() + " or " + signUpRequest.getEmail() + " already exists.");
        }
        log.info("creating user");
        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("created");
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));
        log.info("Saving new user: " + user);

        User result = userRepository.save(user);
            ValidationCode validationCode = new ValidationCode();
            validationCode.setExpiresAt(Instant.now().plus(3, ChronoUnit.HOURS));
            validationCode.setUserId(result.getId());
            validationCode.setCode(UUID.randomUUID().toString());
            validationCodeRepository.save(validationCode);

        return result;
    }

    @Transactional
    public void checkValidationCode(String code) {
        log.info("Checking");
        ValidationCode validationCode = validationCodeRepository
                .findByCode(code).orElseThrow(() ->new AppException("Unable to validate"));
        log.info("Code present");
        if (validationCode.getExpiresAt().isBefore(Instant.now())) {
            log.info("Code expired");
            throw new AppException("Unable to Validate");
        };
        if (!validationCode.getCode().contentEquals(code)) {
            log.info("Code incorect");
            throw new AppException("unable to validate");
        }
        log.debug("All fine...");
        User user = userRepository.findById(validationCode.getUserId()).orElseThrow(() -> new AppException(""));
        user.setEmailValidated(true);
        userRepository.save(user);
    }

    public void createValidationCodeForUserId(UUID userId) {
        User user = userRepository.getById(userId);
        ValidationCode validationCode = validationCodeRepository.findById(userId).orElseGet(() -> new ValidationCode());
        validationCode.setExpiresAt(Instant.now().plus(3, ChronoUnit.HOURS));
        validationCode.setUserId(user.getId());
        validationCode.setCode(UUID.randomUUID().toString());
        validationCodeRepository.save(validationCode);
        log.debug("Created validationcode: " + validationCode.getCode() + " for user: " + user);
    }
}

package net.dancier.dancer.authentication.service;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import net.dancier.dancer.authentication.UserOrEmailAlreadyExistsException;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.*;
import net.dancier.dancer.authentication.repository.PasswordResetCodeRepository;
import net.dancier.dancer.authentication.repository.RoleRepository;
import net.dancier.dancer.authentication.repository.UserRepository;
import net.dancier.dancer.authentication.repository.ValidationCodeRepository;
import net.dancier.dancer.core.exception.AppException;
import net.dancier.dancer.core.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    public static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ValidationCodeRepository validationCodeRepository;

    private final PasswordResetCodeRepository passwordResetCodeRepository;

    public User getUser(UUID userId) {
        try {
            return this.userRepository.getById(userId);
        } catch (EntityNotFoundException entityNotFoundException) {
            throw new NotFoundException("No User Found with this userId: " + userId, entityNotFoundException);
        }
    }

    public User registerUser(RegisterRequestDto signUpRequest) {
        log. info("Checking for existing user: " + signUpRequest.getUsername());
        if(userRepository.findByUsernameOrEmail(signUpRequest.getUsername(), signUpRequest.getEmail()).isPresent()) {
            log.info("User or email already exists.");
            throw new UserOrEmailAlreadyExistsException("User: " + signUpRequest.getUsername() + " or " + signUpRequest.getEmail() + " already exists.");
        }
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User savedUser = userRepository.save(user);
        ValidationCode validationCode = new ValidationCode();
        validationCode.setExpiresAt(Instant.now().plus(3, ChronoUnit.HOURS));
        validationCode.setUserId(savedUser.getId());
        validationCode.setCode(UUID.randomUUID().toString());
        validationCodeRepository.save(validationCode);

        return savedUser;
    }

    public String checkPasswortCodeRequest(String code) {
        PasswordResetCode passwordResetCode = this.passwordResetCodeRepository.findByCode(code).orElseThrow(() ->new AppException("d"));
        RandomString randomString = new RandomString();
        String newPassword = randomString.nextString();
        User user = userRepository.getById(passwordResetCode.getUserId());
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetCodeRepository.delete(passwordResetCode);
        log.info(passwordResetCode.toString());
        return newPassword;
    }

    @Transactional
    public void checkEmailCode(String code) {
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
        validationCodeRepository.delete(validationCode);
        userRepository.save(user);
    }


    public void createEmailValidationCodeForUserOrEmail(String userOrEmail) {
        User user = userRepository.findByUsernameOrEmail(userOrEmail,userOrEmail).orElseThrow(()->new AppException(""));
        createEmailValidationCodeForUser(user);
    }

    public void createEmailValidationCodeForUser(User user) {
        ValidationCode validationCode = validationCodeRepository.findById(user.getId()).orElseGet(() -> new ValidationCode());
        validationCode.setExpiresAt(Instant.now().plus(3, ChronoUnit.HOURS));
        validationCode.setUserId(user.getId());
        validationCode.setCode(UUID.randomUUID().toString());
        validationCodeRepository.save(validationCode);
        log.debug("Created validationcode: " + validationCode.getCode() + " for user: " + user);
    }

    public void createEmailValidationCodeForUserId(UUID userId) {
        User user = userRepository.getById(userId);
        createEmailValidationCodeForUser(user);
    }

    public void createPasswordValidationCodeForUserOrEmail(String userOrEmail) {
        User user = userRepository.findByUsernameOrEmail(userOrEmail, userOrEmail).orElseThrow(() -> new AppException(""));
        PasswordResetCode passwordResetCode = passwordResetCodeRepository.findById(user.getId()).orElseGet(() -> new PasswordResetCode());
        passwordResetCode.setExpiresAt(Instant.now().plus(3, ChronoUnit.HOURS));
        passwordResetCode.setUserId(user.getId());
        passwordResetCode.setCode(UUID.randomUUID().toString());
        passwordResetCodeRepository.save(passwordResetCode);
        log.debug("Create password code: " + passwordResetCode.getCode());
    }

    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }
}

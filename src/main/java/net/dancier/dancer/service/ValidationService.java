package net.dancier.dancer.service;

import net.dancier.dancer.exception.AppException;
import net.dancier.dancer.model.User;
import net.dancier.dancer.model.ValidationCode;
import net.dancier.dancer.repository.UserRepository;
import net.dancier.dancer.repository.ValidationCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class ValidationService {

    public static final Logger log = LoggerFactory.getLogger(ValidationService.class);

    @Autowired
    ValidationCodeRepository validationCodeRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public void checkValidationCode(String code) {
        log.info("Checking");
         ValidationCode validationCode = validationCodeRepository.findByCode(code).orElseThrow(() ->new AppException("Unable to validate"));
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

    }
}

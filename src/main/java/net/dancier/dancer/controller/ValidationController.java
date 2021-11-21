package net.dancier.dancer.controller;

import net.dancier.dancer.exception.AppException;
import net.dancier.dancer.exception.ResourceNotFoundException;
import net.dancier.dancer.model.User;
import net.dancier.dancer.repository.UserRepository;
import net.dancier.dancer.security.CurrentUser;
import net.dancier.dancer.security.UserPrincipal;
import net.dancier.dancer.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("/validation")
public class ValidationController {

    public static Logger log = LoggerFactory.getLogger(ValidationController.class);

    @Autowired
    ValidationService validationService;

    @GetMapping("validate/{validationCode}")
    public void foo(@PathVariable String validationCode) {
        log.info("Got Validation code " + validationCode);
        validationService.checkValidationCode(validationCode);
    }

    @PostMapping()
    public void create(@NotNull @RequestBody String uuid) {
        log.info("sending mail for " + uuid);
        UUID userId = UUID.fromString(uuid);
        validationService.createValidationCodeForUserId(userId);
    }
}

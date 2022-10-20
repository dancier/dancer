package net.dancier.dancer.authentication.event;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.authentication.model.EmailValidationCode;
import net.dancier.dancer.authentication.repository.EmailValidationCodeRepository;
import net.dancier.dancer.mail.service.MailCreationService;
import net.dancier.dancer.mail.service.MailEnqueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NewUserCreatedEventListener {

    private final static Logger log = LoggerFactory.getLogger(NewUserCreatedEventListener.class);

    private final EmailValidationCodeRepository emailValidationCodeRepository;
    private final MailEnqueueService mailEnqueueService;

    private final MailCreationService mailCreationService;

    private final String frontendBaseName;

    @EventListener
    void handle(NewUserCreatedEvent newUserCreatedEvent) {
        Objects.requireNonNull(newUserCreatedEvent.getId());
        EmailValidationCode emailValidationCode = emailValidationCodeRepository
                .findByUserId(newUserCreatedEvent.getId())
                .orElseGet(() -> new EmailValidationCode());
        emailValidationCode.setExpiresAt(Instant
                .now()
                .plus(3, ChronoUnit.HOURS));
        emailValidationCode.setUserId(newUserCreatedEvent.getId());
        emailValidationCode.setCode(UUID.randomUUID().toString());
        emailValidationCodeRepository.save(emailValidationCode);
        enqueueUserMail(newUserCreatedEvent, emailValidationCode.getCode());
        log.debug("Created validation code: " + emailValidationCode.getCode() + " for user: " + newUserCreatedEvent);
    }

    private void enqueueUserMail(NewUserCreatedEvent newUserCreatedEvent, String code) {
        mailEnqueueService.enqueueMail(
                mailCreationService.createDancierMessageFromTemplate(
                        newUserCreatedEvent.getEmail(),
                        MailCreationService.NO_REPLY_FROM,
                        "Dancier - bestätige Deine E-Mail-Adresse!",
                        MailCreationService.NEW_USER_VALIDATE_EMAIL,
                        Map.of( "validationLink", emailValidationLink(code)
                        ))
        );
    }

    private String emailValidationLink(String validationCode) {
        return frontendBaseName + "/registration/verify-account/" + validationCode;
    }

}

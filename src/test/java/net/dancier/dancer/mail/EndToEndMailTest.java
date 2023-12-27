package net.dancier.dancer.mail;

import net.dancier.dancer.AbstractPostgreSQLEnabledTest;
import net.dancier.dancer.mail.service.MailCreationService;
import net.dancier.dancer.mail.service.MailEnqueueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ActiveProfiles;

import jakarta.mail.MessagingException;
import java.util.Map;

@ActiveProfiles({"test", "dev"})
public class EndToEndMailTest extends AbstractPostgreSQLEnabledTest {

    @Autowired
    MailCreationService mailCreationService;

    @Autowired
    MailEnqueueService mailEnqueueService;

    @Test
    public void checkSending() throws MessagingException {
        Map<String, Object> context = Map.of("name", "Marc", "validationLink", "http:/");
        SimpleMailMessage dancierMailMessage = mailCreationService.createDancierMessageFromTemplate(
                "gorzala@gmx.de",
                "no-reply@dancier.net",
                "Bestätige Deine Email-Adresse",
                MailCreationService.NEW_USER_VALIDATE_EMAIL,
                context
        );
        mailEnqueueService.enqueueMail(dancierMailMessage);
    }

}

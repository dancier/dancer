package net.dancier.dancer.mail;

import net.dancier.dancer.AbstractPostgreSQLEnabledTest;
import net.dancier.dancer.mail.service.MailCreationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ActiveProfiles;

import jakarta.mail.MessagingException;
import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

@ActiveProfiles({"test", "dev"})
public class MailCreationTest extends AbstractPostgreSQLEnabledTest {

    @Autowired
    MailCreationService mailCreationService;

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
        then(dancierMailMessage).isNotNull();
    }

}

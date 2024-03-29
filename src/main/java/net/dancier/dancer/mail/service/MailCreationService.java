package net.dancier.dancer.mail.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MailCreationService {

    private static Logger log = LoggerFactory.getLogger(MailCreationService.class);

    public final static String NEW_USER_VALIDATE_EMAIL = "text/new-user-validate-email";

    public final static String USER_ALREADY_EXISTS_EMAIL = "text/new-user-already-exists-email";

    public final static String PASSWORD_CHANGE_REQUEST_EMAIL = "text/password-change-request-email";

    public final static String CONTACT_FORMULAR = "text/contact-formular";

    public final static String CONTACT_FORMULAR_FEEDBACK = "text/contact-formular-feedback";

    public final static String NO_REPLY_FROM = "no-reply@dancier.net";

    private final TemplateEngine emailTemplateEngine;

    public SimpleMailMessage createDancierMessageFromTemplate(
            String to,
            String from,
            String subject,
            String bodyTemplate,
            Map<String, Object> context) {

        final SimpleMailMessage dancierMailMessage = new SimpleMailMessage();
        dancierMailMessage.setFrom(from);
        dancierMailMessage.setTo(to);
        dancierMailMessage.setSubject(subject);
        final Context thymeleafContext = new Context(Locale.GERMAN, context);
        dancierMailMessage.setText(
                this.emailTemplateEngine.process(bodyTemplate, thymeleafContext)
        );
        return dancierMailMessage;
    }

}

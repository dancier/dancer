package net.dancier.dancer.mail.configuration;

import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.io.InputStream;

public class DumpingMailSender implements JavaMailSender {

    private static final Logger log = LoggerFactory.getLogger(DumpingMailSender.class);

    @Override
    public MimeMessage createMimeMessage() {
        throw new NotImplementedException();
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        log.debug("Sending Mail: " + simpleMessage);
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        throw new NotImplementedException();
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        throw new NotImplementedException();
    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {
        throw new NotImplementedException();
    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
        throw new NotImplementedException();
    }

    @Override
    public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
        throw new NotImplementedException();
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
        throw new NotImplementedException();
    }
}

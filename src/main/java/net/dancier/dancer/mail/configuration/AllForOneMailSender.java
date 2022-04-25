package net.dancier.dancer.mail.configuration;

import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Properties;

public class AllForOneMailSender implements JavaMailSender {

    private static final Logger log = LoggerFactory.getLogger(AllForOneMailSender.class);

    private final JavaMailSenderImpl springJavaMailSenderImpl;

    private final String allForOneAddress;

    public AllForOneMailSender(String hostname, Integer port, String user, String pass, String allForOneAddress) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(hostname);
        javaMailSender.setPort(Integer.valueOf(port));

        javaMailSender.setUsername(user);
        javaMailSender.setPassword(pass);

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "true");
        this.allForOneAddress = allForOneAddress;
        this.springJavaMailSenderImpl = javaMailSender;
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        simpleMessage.setBcc();
        simpleMessage.setCc();
        simpleMessage.setFrom(allForOneAddress);
        simpleMessage.setReplyTo(allForOneAddress);
        simpleMessage.setTo(allForOneAddress);
        log.debug("Sending mail: " + simpleMessage);
        this.springJavaMailSenderImpl.send(simpleMessage);
    }

    @Override
    public MimeMessage createMimeMessage() {
        throw new NotYetImplementedException();
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        throw new NotYetImplementedException();
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        throw new NotYetImplementedException();
    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {
        throw new NotYetImplementedException();
    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
        throw new NotYetImplementedException();
    }

    @Override
    public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
        throw new NotYetImplementedException();
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
        throw new NotYetImplementedException();
    }
}

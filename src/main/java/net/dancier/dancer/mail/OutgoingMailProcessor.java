package net.dancier.dancer.mail;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.mail.model.OutgoingMail;
import net.dancier.dancer.mail.repository.OutgoingMailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class OutgoingMailProcessor {

    private final Logger log = LoggerFactory.getLogger(OutgoingMailProcessor.class);

    private final OutgoingMailRepository outgoingMailRepository;

    private final JavaMailSender javaMailSender;

    @Scheduled(fixedRate = 10000)
    public void process() {
        log.debug("Processing outgoing mails...");
        Collection<OutgoingMail> outgoingMailList = outgoingMailRepository.selectToProcess();
        for(OutgoingMail outgoingMail: outgoingMailList) {
            log.debug("About to send this mail...");
            outgoingMail.setRetry(outgoingMail.getRetry()+1);
            try {
                this.javaMailSender.send(outgoingMail.getMail());
            } catch (MailException mailException) {
            }
        }
    }
}

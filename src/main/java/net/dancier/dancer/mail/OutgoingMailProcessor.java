package net.dancier.dancer.mail;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.mail.model.OutgoingMail;
import net.dancier.dancer.mail.model.OutgoingMailStatus;
import net.dancier.dancer.mail.repository.OutgoingMailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailAuthenticationException;
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
        Collection<OutgoingMail> outgoingMailList = outgoingMailRepository.lockAndList();
        log.debug("Got this: " + outgoingMailList);
        for(OutgoingMail outgoingMail: outgoingMailList) {
            processOneMail(outgoingMail);
        }
    }

    private void processOneMail(OutgoingMail outgoingMail) {
        log.debug("About to send this mail...");
        outgoingMail.setRetry(outgoingMail.getRetry() + 1);
        try {
            this.javaMailSender.send(outgoingMail.getMail());
            outgoingMail.setStatus(OutgoingMailStatus.OK);
        } catch (MailAuthenticationException mailAuthenticationException) {
            outgoingMail.setStatus(OutgoingMailStatus.TEMPORARY_FAILED);
            log.error("Problem with password." + mailAuthenticationException);
        } catch (MailException mailException) {
            outgoingMail.setStatus(OutgoingMailStatus.FINALLY_FAILED);
            log.error("Some: " + mailException);
        }
        outgoingMailRepository.save(outgoingMail);
    }
}

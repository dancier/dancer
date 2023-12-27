package net.dancier.dancer.mail.service;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.mail.model.DancierMailMessage;
import net.dancier.dancer.mail.repository.OutgoingMailRepository;
import net.dancier.dancer.mail.model.OutgoingMail;
import net.dancier.dancer.mail.model.OutgoingMailStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailEnqueueService {

    private static Logger log = LoggerFactory.getLogger(MailEnqueueService.class);

    private final OutgoingMailRepository outgoingMailRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    public void enqueueMail(DancierMailMessage dancierMailMessage) {
        OutgoingMail outgoingMail = new OutgoingMail();
        outgoingMail.setStatus(OutgoingMailStatus.QUEUED);
        outgoingMail.setRetry(0);
        outgoingMail.setMail(dancierMailMessage);
        this.outgoingMailRepository.save(outgoingMail);
        applicationEventPublisher.publishEvent(dancierMailMessage);
    }

}

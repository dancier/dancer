package net.dancier.dancer.core.events;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.ScheduleMessagePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApplicationEventListener {

    public static final Logger log = LoggerFactory.getLogger(ApplicationEventListener.class);
    private static final URI SOURCE = URI.create("http://dancer.dancier.net");

    private final ScheduleMessagePort scheduleMessagePort;

    @EventListener
    public void handle(SimpleMailMessage dancierMailMessage) {
        scheduleMessagePort.schedule(
                dancierMailMessage,
                UUID.randomUUID().toString(),
                SOURCE,
                "email-sending-requested"
        );
    }

    @EventListener
    public void handle(ProfileUpdatedEvent profileUpdatedEvent) {
        log.info("Got a Profile Change: {}", profileUpdatedEvent);
        scheduleMessagePort.schedule(
                profileUpdatedEvent,
                profileUpdatedEvent.getDancer().getId().toString(),
                SOURCE,
                "profile-updated");
    }
}

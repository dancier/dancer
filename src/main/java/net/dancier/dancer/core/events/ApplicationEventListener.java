package net.dancier.dancer.core.events;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.ScheduleMessagePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class ApplicationEventListener {

    public static final Logger log = LoggerFactory.getLogger(ApplicationEventListener.class);
    private static final URI SOURCE = URI.create("http://dancer.dancier.net");

    private final ScheduleMessagePort scheduleMessagePort;

    @EventListener
    @Transactional
    public void handle(ProfileUpdatedEvent profileUpdatedEvent) {
        log.info("Got a Profile Change: {}", profileUpdatedEvent);
        scheduleMessagePort.schedule(
                profileUpdatedEvent,
                profileUpdatedEvent.getDancer().getId().toString(),
                SOURCE,
                "profile-updated");
    }
}

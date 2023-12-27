package net.dancier.dancer.core.events;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.ScheduleMessagePort;
import net.dancier.dancer.eventlog.service.EventlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class ApplicationEventListener {

    public static final Logger log = LoggerFactory.getLogger(ApplicationEventListener.class);

    public static final URI FRONTEND_SOURCE = URI.create("http://dancier.net");
    private static final URI BACKEND_SOURCE = URI.create("http://dancer.dancier.net");

    private final EventlogService eventlogService;

    private final EventCreator eventCreator;

    private final ScheduleMessagePort scheduleMessagePort;

    @EventListener
    @Transactional
    public void handle(ProfileUpdatedEvent profileUpdatedEvent) {
        log.info("Got a Profile Change: {}", profileUpdatedEvent);
        scheduleMessagePort.schedule(
                profileUpdatedEvent,
                profileUpdatedEvent.getDancer().getId().toString(),
                BACKEND_SOURCE,
                "profile-updated");
    }
}

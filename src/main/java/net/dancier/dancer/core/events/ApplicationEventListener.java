package net.dancier.dancer.core.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.ScheduleMessagePort;
import net.dancier.dancer.eventlog.service.EventlogService;
import net.dancier.dancer.messaging.ScheduleMessageAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApplicationEventListener {

    public static final Logger log = LoggerFactory.getLogger(ApplicationEventListener.class);

    private static final URI FRONTEND_SOURCE = URI.create("http://dancier.net");
    private static final URI BACKEND_SOURCE = URI.create("http://dancer.dancier.net");

    private final EventlogService eventlogService;

    private final EventCreator eventCreator;

    private final ScheduleMessagePort scheduleMessagePort;

    private final ObjectMapper objectMapper;

    @EventListener
    @Transactional
    public void handle(ProfileUpdatedEvent profileUpdatedEvent) {
        log.info("Got a Profile Change: {}", profileUpdatedEvent);
        eventlogService.appendNew(
                eventCreator.createEventlog(
                        "profile-updated",
                        profileUpdatedEvent.getDancer()));
        try {
            String tmp = objectMapper.writeValueAsString(profileUpdatedEvent);
            System.out.println(tmp);
            scheduleMessagePort.schedule(
                    profileUpdatedEvent,
                    profileUpdatedEvent.getDancer().getId().toString(),
                    BACKEND_SOURCE,
                    "profile-updated");
        } catch (JsonProcessingException jpe) {
            log.error("Unable to generate Cloud-Event for: " + profileUpdatedEvent, jpe);
            throw new ApplicationContextException("Unable to create Json", jpe);
        }
    }

}

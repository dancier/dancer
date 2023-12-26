package net.dancier.dancer.core.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.service.EventlogService;
import net.dancier.dancer.messaging.ScheduleMessageAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProfileUpdateEventListener {

    public static final Logger log = LoggerFactory.getLogger(ProfileUpdateEventListener.class);

    private final EventlogService eventlogService;

    private final EventCreator eventCreator;

    private final ScheduleMessageAdapter scheduleMessageAdapter;

    private final ObjectMapper objectMapper;

    @EventListener
    @Transactional
    public void handle(ProfileUpdatedEvent profileUpdatedEvent) {
        log.info("Got a Profile Change");
        eventlogService.appendNew(
                eventCreator.createEventlog(
                        "profile-updated",
                        profileUpdatedEvent.getDancer()));
        try {
            CloudEvent cloudEvent = CloudEventBuilder
                    .v1()
                    .withId(UUID.randomUUID().toString())
                    .withSource(URI.create("F"))
                    .withType("profile-updated")
                    .withData(objectMapper.writeValueAsBytes(profileUpdatedEvent)).build();
            scheduleMessageAdapter.schedule(cloudEvent, profileUpdatedEvent.getDancer().getId().toString());
        } catch (JsonProcessingException jpe) {
            log.error("Unable to generate Cloud-Event for: " + profileUpdatedEvent, jpe);
            throw new ApplicationContextException("Unable to create Json", jpe);
        }
    }

}

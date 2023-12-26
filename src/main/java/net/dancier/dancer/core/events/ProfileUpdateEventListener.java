package net.dancier.dancer.core.events;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.service.EventlogService;
import net.dancier.dancer.messaging.ScheduleMessageAdapter;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileUpdateEventListener {

    private final EventlogService eventlogService;

    private final EventCreator eventCreator;

    private final ScheduleMessageAdapter scheduleMessageAdapter;

    @EventListener
    @Transactional
    public void handle(ProfileUpdatedEvent profileUpdatedEvent) {
        eventlogService.appendNew(
                eventCreator.createEventlog(
                        "profile-updated",
                        profileUpdatedEvent.getDancer()));
        scheduleMessageAdapter.schedule(profileUpdatedEvent, null, null);
    }

}

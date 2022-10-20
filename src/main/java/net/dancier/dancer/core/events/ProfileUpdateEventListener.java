package net.dancier.dancer.core.events;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.EventlogService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileUpdateEventListener {

    private final EventlogService eventlogService;

    private final EventCreator eventCreator;

    @EventListener
    public void handle(ProfileUpdatedEvent profileUpdatedEvent) {
        eventlogService.createNew(
                eventCreator.createEventlogDto(
                        "profile-updated",
                        profileUpdatedEvent.getDancer()));
    }

}

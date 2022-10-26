package net.dancier.dancer.eventlog.dto;

import net.dancier.dancer.eventlog.model.Eventlog;

public class EventlogMapper {

    public static Eventlog toEventlog(NewEventlogDto newEventlogDto) {
        Eventlog eventlog = new Eventlog();
        eventlog.setTopic(newEventlogDto.getTopic());
        eventlog.setMetaData(newEventlogDto.getMetaData());
        eventlog.setPayload(newEventlogDto.getPayload());
        return eventlog;
    }
}

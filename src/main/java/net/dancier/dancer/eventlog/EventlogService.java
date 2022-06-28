package net.dancier.dancer.eventlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
public class EventlogService {
    @Autowired
    EventlogDAO eventlogDAO;

    public void createNew(EventlogDto eventlogDto) throws SQLException {
        EventlogEntry eventlogEntry = new EventlogEntry();
        eventlogEntry.setId(UUID.randomUUID());
        eventlogEntry.setTopic(eventlogDto.getTopic());
        eventlogEntry.setPayload(eventlogDto.getPayload());
        eventlogEntry.setMetaData(eventlogDto.getMetaData());
        eventlogEntry.setCreated(Instant.now());
        if (eventlogDto.getRoles()!=null) {
            eventlogEntry.setRoles(eventlogDto.getRoles());
        } else {
            eventlogEntry.setRoles(Set.of());
        }
        this.eventlogDAO.publish(eventlogEntry);
    }
}

package net.dancier.dancer.eventlog.service;

import net.dancier.dancer.core.exception.AppliationException;
import net.dancier.dancer.eventlog.repository.EventlogDAO;
import net.dancier.dancer.eventlog.model.Eventlog;
import net.dancier.dancer.eventlog.repository.EventlogEntry;
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

    public void appendNew(Eventlog eventlogDto) {
        try {
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
            eventlogEntry.setUserId(eventlogDto.getUserId());
            this.eventlogDAO.schedule(eventlogEntry);
        } catch (SQLException sqlException) {
            throw new AppliationException("Unable to create new Eventlog-Entry.", sqlException);
        }
    }
}

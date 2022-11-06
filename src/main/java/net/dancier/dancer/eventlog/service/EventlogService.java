package net.dancier.dancer.eventlog.service;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.exception.AppliationException;
import net.dancier.dancer.eventlog.model.Eventlog;
import net.dancier.dancer.eventlog.repository.EventlogDAO;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventlogService {

    private final EventlogDAO eventlogDAO;

    public void appendNew(Eventlog eventlog) {
        try {
            eventlog.setId(UUID.randomUUID());
            eventlog.setCreated(Instant.now());
            this.eventlogDAO.schedule(eventlog);
        } catch (SQLException sqlException) {
            throw new AppliationException("Unable to create new Eventlog-Entry.", sqlException);
        }
    }

}
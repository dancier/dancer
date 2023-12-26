package net.dancier.dancer.eventlog.service;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.exception.ApplicationException;
import net.dancier.dancer.eventlog.model.Eventlog;
import net.dancier.dancer.eventlog.repository.EventlogDAO;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventlogService {

    private final EventlogDAO eventlogDAO;

    private final static Set ALL_ALLOWED_TOPICS = Set.of("Foo");
    private final static Set TOPICS_THAT_REQUIRE_A_USER = Set.of();

    public void appendNew(Eventlog eventlog) {

        try {
            eventlog.setId(UUID.randomUUID());
            eventlog.setCreated(Instant.now());
            this.eventlogDAO.schedule(eventlog);
        } catch (SQLException sqlException) {
            throw new ApplicationException("Unable to create new Eventlog-Entry.", sqlException);
        }
    }

    private void validateTopic(Eventlog eventlog) {

    }
    private void authorize(Eventlog eventlog) {
        String topic = eventlog.getTopic();
    }

}
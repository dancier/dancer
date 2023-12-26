package net.dancier.dancer.eventlog.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.authentication.model.Role;
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

    private final static Set<String> DEFAULT_AUTHENTICATED = Set.of();
    private final static Set<EventlogConfig> allowedEvents = Set.of(
            EventlogConfig.of("profile-updated", DEFAULT_AUTHENTICATED)
    );

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

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    private static class EventlogConfig {
        public static EventlogConfig of(String name, Set<String> roles) {
            return new EventlogConfig(name, roles);
        }
        private String name;
        private Set<String> neededRoles;
    }
}
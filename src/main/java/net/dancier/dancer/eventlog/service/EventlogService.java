package net.dancier.dancer.eventlog.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.authentication.model.Role;
import net.dancier.dancer.core.exception.ApplicationException;
import net.dancier.dancer.eventlog.model.Eventlog;
import net.dancier.dancer.eventlog.repository.EventlogDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventlogService {

    private final Logger log = LoggerFactory.getLogger(EventlogService.class);

    private final EventlogDAO eventlogDAO;

    private final static Set<String> DEFAULT_AUTHENTICATED = Set.of("ROLE_USER", "ROLE_ADMIN");
    private final static Set<String> AT_LEAST_HUMAN = Set.of("ROLE_HUMAN", "ROLE_USER", "ROLE_ADMIN");
    private final static Set<String> NO_SPECIAL_ROLE_NEEDED = Set.of();
    private final static Set<EventlogConfig> allowedEvents = Set.of(
            EventlogConfig.of("app_instance_id_created", NO_SPECIAL_ROLE_NEEDED),
            EventlogConfig.of("navigated_to_page", NO_SPECIAL_ROLE_NEEDED),
            EventlogConfig.of("human_session_created", AT_LEAST_HUMAN),
            EventlogConfig.of("contact_message_sent", AT_LEAST_HUMAN),
            EventlogConfig.of("profile_updated", DEFAULT_AUTHENTICATED) // will not go over the eventlog stuff in the future...
    );

    public void appendNew(Eventlog eventlog) {
        validateTopic(eventlog);
        authorize(eventlog);
        try {
            eventlog.setId(UUID.randomUUID());
            eventlog.setCreated(Instant.now());
            this.eventlogDAO.schedule(eventlog);
        } catch (SQLException sqlException) {
            throw new ApplicationException("Unable to create new Eventlog-Entry.", sqlException);
        }
    }

    private void validateTopic(Eventlog eventlog) {
        String topic = eventlog.getTopic();
        log.info("Validating Topic: {}", eventlog.getTopic());
    }
    private void authorize(Eventlog eventlog) {
        String topic = eventlog.getTopic();
        log.info("Authorizing eventlog request: {}", topic);
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
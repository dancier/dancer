package net.dancier.dancer.eventlog.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.ScheduleMessagePort;
import net.dancier.dancer.core.exception.ApplicationException;
import net.dancier.dancer.core.exception.BusinessException;
import net.dancier.dancer.eventlog.model.Eventlog;
import net.dancier.dancer.eventlog.repository.EventlogDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.dancier.dancer.core.events.ApplicationEventListener.FRONTEND_SOURCE;

@Service
@RequiredArgsConstructor
public class EventlogService {

    private final Logger log = LoggerFactory.getLogger(EventlogService.class);

    private final EventlogDAO eventlogDAO;

    private final ScheduleMessagePort scheduleMessagePort;
    private final static Set<String> DEFAULT_AUTHENTICATED = Set.of("ROLE_USER", "ROLE_ADMIN");
    private final static Set<String> AT_LEAST_HUMAN = Set.of("ROLE_HUMAN", "ROLE_USER", "ROLE_ADMIN");
    private final static Set<String> NO_SPECIAL_ROLE_NEEDED = Set.of("ROLE_ANONYMOUS");
    private final static Set<EventlogConfig> allowedEvents = Set.of(
            EventlogConfig.of("app_instance_id_created", NO_SPECIAL_ROLE_NEEDED),
            EventlogConfig.of("navigated_to_page", NO_SPECIAL_ROLE_NEEDED),
            EventlogConfig.of("human_session_created", AT_LEAST_HUMAN),
            EventlogConfig.of("contact_message_sent", AT_LEAST_HUMAN),

            EventlogConfig.of("app-instance-id-created", NO_SPECIAL_ROLE_NEEDED),
            EventlogConfig.of("navigated-to-page", NO_SPECIAL_ROLE_NEEDED),
            EventlogConfig.of("human-session-created", AT_LEAST_HUMAN),
            EventlogConfig.of("contact-message-sent", AT_LEAST_HUMAN)
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
        log.info("Now scheduling..." + eventlog);
        scheduleMessagePort.schedule(
                eventlog,
                eventlog.getId().toString(),
                FRONTEND_SOURCE,
                eventlog.getTopic().replaceAll("_", "-")); // as long halbe kanne is still sending old event format
    }

    private void validateTopic(Eventlog eventlog) {
        String topic = eventlog.getTopic();
        log.info("Validating Topic: {}", eventlog.getTopic());
        if (!allowedEvents.stream()
                .map(EventlogConfig::getName)
                .collect(Collectors.toSet())
                .contains(topic)) {
            throw new BusinessException("this eventlog topic is not allowed at all: " + topic);
        }

    }
    private void authorize(Eventlog eventlog) {
        String topic = eventlog.getTopic();
        Set<String> usedRoles = eventlog.getRoles();
        Set<String> neededRoles = allowedEvents
                .stream()
                .filter(eventlogConfig -> eventlogConfig.name.equals(topic))
                .flatMap(eventlogConfig -> eventlogConfig.neededRoles.stream())
                .collect(Collectors.toSet());
        log.info("Authorizing eventlog request: {}", topic);
        Boolean authorized = usedRoles.stream().anyMatch(usedRole -> neededRoles.contains(usedRole));
        if (!authorized) {
            throw new BusinessException("We got this roles: " + usedRoles + " but needed this:" + neededRoles);
        }
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
package net.dancier.dancer.core.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.AppInstanceIdFilter;
import net.dancier.dancer.eventlog.EventlogDto;
import net.dancier.dancer.security.AuthenticatedUser;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventCreator {

    private final ObjectMapper objectMapper;

    public EventlogDto createEventlogDto(String eventType, Object payload) {
        EventlogDto eventlogDto = new EventlogDto();
        setUserAndRoles(eventlogDto);
        setMetaData(eventlogDto);
        eventlogDto.setTopic(eventType);
        eventlogDto.setPayload(objectMapper.convertValue(payload, JsonNode.class));
        return eventlogDto;
    }

    private void setMetaData(EventlogDto eventlogDto) {
        Map<String, String> metaData = new HashMap<>();
        metaData.put("appInstanceId", MDC.get(AppInstanceIdFilter.APP_INSTANCE_ID_CONTEXT_FIELD));
        metaData.put("sourceTime", Instant.now().toString());
        eventlogDto.setMetaData(objectMapper.convertValue(metaData, JsonNode.class));
    }

    private void setUserAndRoles(EventlogDto eventlogDto) {
        try {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            eventlogDto.setUserId(authenticatedUser.getUserId());
            eventlogDto.setRoles(authenticatedUser.getAuthorities().stream().map(Objects::toString).collect(Collectors.toSet()));
        } catch (Exception e) {}
    }
}

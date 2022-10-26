package net.dancier.dancer.eventlog.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.model.EventlogEntry;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventlogEntryRowMapper implements RowMapper<EventlogEntry> {

    private final ObjectMapper objectMapper;

    @Override
    public EventlogEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        EventlogEntry eventlogEntry = new EventlogEntry();
        try {
            eventlogEntry.setId(rs.getObject("id", UUID.class));
        eventlogEntry.setTopic(rs.getString("topic"));
            eventlogEntry.setMetaData(objectMapper.readValue(rs.getString("meta_data"), JsonNode.class));
        eventlogEntry.setPayload(objectMapper.readValue(rs.getString("payload"), JsonNode.class));
        eventlogEntry.setCreated(rs.getTimestamp("created").toInstant());
        eventlogEntry.setUserId(rs.getObject("user_id", UUID.class));
        eventlogEntry.setRoles(arrayToString(rs.getArray("roles")));
        eventlogEntry.setStatus(EventlogEntryStatus.valueOf(rs.getString("status")));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return eventlogEntry;
    }
    private Set<String> arrayToString(Array array) throws SQLException {
        String[] roles = (String[])array.getArray();
        Set<String> result = new HashSet<>();
        for(Object s: roles) {
            result.add(s.toString());
        }
        return result;
    }
}

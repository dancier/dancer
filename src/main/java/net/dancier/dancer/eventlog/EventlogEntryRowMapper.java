package net.dancier.dancer.eventlog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EventlogEntryRowMapper implements RowMapper<EventlogEntry> {

    private final ObjectMapper objectMapper = new ObjectMapper();

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
        eventlogEntry.setRoles(new HashSet(Arrays.asList(rs.getArray("roles"))));
        eventlogEntry.setStatus(EventlogEntryStatus.valueOf(rs.getString("status")));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return eventlogEntry;
    }
}

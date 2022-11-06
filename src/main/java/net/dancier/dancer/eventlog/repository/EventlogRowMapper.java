package net.dancier.dancer.eventlog.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.model.Eventlog;
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
public class EventlogRowMapper implements RowMapper<Eventlog> {

    private final ObjectMapper objectMapper;

    @Override
    public Eventlog mapRow(ResultSet rs, int rowNum) throws SQLException {
        Eventlog eventlog = new Eventlog();
        try {
            eventlog.setId(rs.getObject("id", UUID.class));
            eventlog.setTopic(rs.getString("topic"));
            eventlog.setMetaData(objectMapper.readValue(rs.getString("meta_data"), JsonNode.class));
            eventlog.setPayload(objectMapper.readValue(rs.getString("payload"), JsonNode.class));
            eventlog.setCreated(rs.getTimestamp("created").toInstant());
            eventlog.setUserId(rs.getObject("user_id", UUID.class));
            eventlog.setRoles(arrayToString(rs.getArray("roles")));
            eventlog.setStatus(EventlogStatus.valueOf(rs.getString("status")));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return eventlog;
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
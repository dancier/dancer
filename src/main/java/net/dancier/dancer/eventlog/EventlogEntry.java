package net.dancier.dancer.eventlog;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import net.dancier.dancer.authentication.model.Role;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class EventlogEntry {

    private UUID id;

    private UUID userId;

    private Set<String> roles;

    private String topic;

    private JsonNode metaData;

    private JsonNode payload;

    private Instant created;
}
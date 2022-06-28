package net.dancier.dancer.eventlog;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import net.dancier.dancer.authentication.model.Role;

import java.util.Set;
import java.util.UUID;

@Data
public class EventlogDto {

    private String topic;

    private JsonNode metaData;

    private JsonNode payload;

    private Set<String> roles;

    private UUID userId;
}

package net.dancier.dancer.eventlog.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.Data;
import net.dancier.dancer.authentication.model.Role;

import java.util.Set;
import java.util.UUID;

@Data
public class EventlogDto {

    private String topic;

    private JsonNode metaData = JsonNodeFactory.instance.objectNode();

    private JsonNode payload = JsonNodeFactory.instance.objectNode();

    private Set<String> roles;

    private UUID userId;
}

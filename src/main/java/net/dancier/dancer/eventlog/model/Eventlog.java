package net.dancier.dancer.eventlog.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class Eventlog {

    private String topic;

    private JsonNode metaData = JsonNodeFactory.instance.objectNode();

    private JsonNode payload = JsonNodeFactory.instance.objectNode();

    private Set<String> roles;

    private UUID userId;
}

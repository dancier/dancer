package net.dancier.dancer.eventlog.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.Data;

@Data
public class NewEventlogDto {

    private String topic;

    private JsonNode metaData = JsonNodeFactory.instance.objectNode();

    private JsonNode payload = JsonNodeFactory.instance.objectNode();

}
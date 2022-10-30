package net.dancier.dancer.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class MessageDto {
    private UUID id;
    private UUID author;
    private String text;
    private List<UUID> readByDancers;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private OffsetDateTime creationTimestamp;

}

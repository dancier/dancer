package net.dancier.dancer.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class MessageDto {
    private UUID id;
    private UUID authorId;
    private String text;
    private List<UUID> readByParticipants;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdAt;

}

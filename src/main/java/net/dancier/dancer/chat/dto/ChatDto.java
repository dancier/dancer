package net.dancier.dancer.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ChatDto {
    private UUID chatId;
    private List<UUID> participantIds;
    private OffsetDateTime lastActivity;
    private MessageDto lastMessage;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdAt;
}

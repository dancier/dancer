package net.dancier.dancer.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ChatDto {
    private UUID chatId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private OffsetDateTime lastActivity;
    @Enumerated(EnumType.STRING)
    private DanceType type;
    private List<MessageDto> messages;
    private MessageDto lastMessage;
    private List<DancerDto> dancers;
}

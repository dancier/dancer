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
    private List<UUID> participantIds;
//2023-12-15T13:00:23.897543763Z
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    //private OffsetDateTime lastActivity;
    private MessageDto lastMessage;
    private OffsetDateTime createdAt;
}

package net.dancier.dancer.chat.dto;

import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
import java.util.UUID;

@Data
public class CreateChatDto {
    private List<UUID> dancerIds;
    @Enumerated(EnumType.STRING)
    private ChatType type;
}

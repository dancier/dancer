package net.dancier.dancer.chat.dto;

import lombok.Data;

import java.util.List;

@Data
public class MessagesDto {
    private List<MessageDto> messages;
}

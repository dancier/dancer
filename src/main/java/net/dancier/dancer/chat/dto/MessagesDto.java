package net.dancier.dancer.chat.dto;

import lombok.Data;

import java.util.Set;

@Data
public class MessagesDto {
    private Set<MessageDto> messages;
}

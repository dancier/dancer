package net.dancier.dancer.chat.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MessageDto {

    String chatId;
    String chatMessage;
    UUID dancerId;

}

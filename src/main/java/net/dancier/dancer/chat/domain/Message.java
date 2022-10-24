package net.dancier.dancer.chat.domain;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Message {

    UUID dancerId;
    UUID chatId;
    String content;
}

package net.dancier.dancer.chat;

import net.dancier.dancer.chat.dto.ChatDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    public List<ChatDto> getChatsByUserId(UUID userId) {
        return Collections.emptyList();
    }
}

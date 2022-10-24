package net.dancier.dancer.chat;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.chat.domain.Chat;
import net.dancier.dancer.chat.domain.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatDao chatDao;

    public UUID createChat(String name) {
        return chatDao.saveNewChat(name);
    }

    public void addMessageToChat(Message message, UUID chatId) {
        chatDao.addMessageToChat(message, chatId);
    }

    public List<Chat> getAllChats() {
        return chatDao.getAllChats();
    }

    public List<Message> getAllMessageOfAChat(UUID chatId) {
        return null;
    }
}

package net.dancier.dancer.chat;

import net.dancier.dancer.chat.domain.Chat;
import net.dancier.dancer.chat.domain.Message;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ChatDaoStubbed implements ChatDao{

    List<Chat> allChats = new LinkedList<>();
    Map<UUID, List<Message>> messageToChat = new HashMap<>();


    @Override
    public UUID saveNewChat(String name) {
        Chat chat = Chat.builder().name(name).id(UUID.randomUUID()).build();
        allChats.add(chat);
        messageToChat.put(chat.getId(), List.of());
        return chat.getId();
    }

    @Override
    public List<Chat> getAllChats() {
        return this.allChats;
    }

    @Override
    public void addMessageToChat(Message message, UUID chatId ) {
        messageToChat.get(chatId).add(message);
    }

}

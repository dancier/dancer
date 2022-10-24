package net.dancier.dancer.chat;

import net.dancier.dancer.chat.domain.Chat;
import net.dancier.dancer.chat.domain.Message;

import java.util.List;
import java.util.UUID;

public interface ChatDao {
    UUID saveNewChat(String name);


    List<Chat> getAllChats();

    void addMessageToChat (Message message, UUID chatId);

}


package net.dancier.dancer.chat;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.chat.client.ChatServiceClient;
import net.dancier.dancer.chat.client.RemoteCreateMessageDto;
import net.dancier.dancer.chat.dto.*;
import net.dancier.dancer.core.DancerService;
import net.dancier.dancer.core.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final static Logger log = LoggerFactory.getLogger(ChatService.class);

    private final ChatServiceClient chatServiceClient;

    public ChatsDto getChatsByUserId(UUID dancerId) {
        log.info("Getting Chats for: " + dancerId);
        return chatServiceClient.getChats(dancerId);
    }

    public ChatDto createChat(UUID dancerId, CreateChatDto createChatDto) {
        throwIfDancerIsNotInChat(createChatDto.getDancerIds(), dancerId);

        return chatServiceClient.createChat(createChatDto);
    }

    public ChatDto getChat(UUID chatId, UUID dancerId) {
        ChatDto chat = chatServiceClient.getChat(chatId);

        throwIfDancerIsNotInChat(chat.getDancerIds(), dancerId);

        return chat;
    }

    public MessagesDto getMessages(UUID chatId, UUID dancerId, Optional<UUID> lastMessageId) {
        ChatDto chat = chatServiceClient.getChat(chatId);

        throwIfDancerIsNotInChat(chat.getDancerIds(), dancerId);

        MessagesDto messages = chatServiceClient.getMessages(chatId, dancerId, lastMessageId);
        return messages;
    }

    public Void createMessage(UUID chatId, UUID dancerId, CreateMessageDto createMessageDto) {
        ChatDto chat = chatServiceClient.getChat(chatId);

        throwIfDancerIsNotInChat(chat.getDancerIds(), dancerId);

        RemoteCreateMessageDto remoteCreateMessageDto = new RemoteCreateMessageDto.RemoteCreateMessageDtoBuilder()
                .fromCreateMessageDto(createMessageDto)
                .withAuthorId(dancerId)
                .build();

        return chatServiceClient.createMessage(chatId, remoteCreateMessageDto);
    }

    private void throwIfDancerIsNotInChat(List<UUID> dancerIds, UUID currentDancerId) {
        if (!dancerIds.contains(currentDancerId)) {
            throw new BusinessException("Current dancer must be part of the chat");
        }
    }

}

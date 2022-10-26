package net.dancier.dancer.chat;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.chat.client.ChatServiceClient;
import net.dancier.dancer.chat.client.RemoteCreateMessageDto;
import net.dancier.dancer.chat.dto.*;
import net.dancier.dancer.core.DancerService;
import net.dancier.dancer.core.exception.BusinessException;
import net.dancier.dancer.core.model.Dancer;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final DancerService dancerService;

    private final ChatServiceClient chatServiceClient;

    public ChatsDto getChatsByUserId(UUID userId) {
        Dancer currentDancer = dancerService.loadByUserId(userId);
        return chatServiceClient.getChats(currentDancer.getId());
    }

    public ChatDto createChat(UUID userId, CreateChatDto createChatDto) {
        Dancer currentDancer = dancerService.loadByUserId(userId);

        throwIfDancerIsNotInChat(createChatDto.getDancerIds(), currentDancer);

        return chatServiceClient.createChat(createChatDto);
    }

    public ChatDto getChat(UUID chatId, UUID userId) {
        Dancer currentDancer = dancerService.loadByUserId(userId);
        ChatDto chat = chatServiceClient.getChat(chatId);

        throwIfDancerIsNotInChat(chat.getDancerIds(), currentDancer);

        return chat;
    }

    public MessagesDto getMessages(UUID chatId, UUID userId, Optional<UUID> lastMessageId) {
        Dancer currentDancer = dancerService.loadByUserId(userId);
        ChatDto chat = chatServiceClient.getChat(chatId);

        throwIfDancerIsNotInChat(chat.getDancerIds(), currentDancer);

        MessagesDto messages = chatServiceClient.getMessages(chatId, currentDancer.getId(), lastMessageId);
        return messages;
    }

    public Void createMessage(UUID chatId, UUID userId, CreateMessageDto createMessageDto) {
        Dancer currentDancer = dancerService.loadByUserId(userId);
        ChatDto chat = chatServiceClient.getChat(chatId);

        throwIfDancerIsNotInChat(chat.getDancerIds(), currentDancer);

        RemoteCreateMessageDto remoteCreateMessageDto = new RemoteCreateMessageDto.RemoteCreateMessageDtoBuilder()
                .fromCreateMessageDto(createMessageDto)
                .withAuthor(currentDancer.getId())
                .build();

        return chatServiceClient.createMessage(chatId, remoteCreateMessageDto);
    }

    private void throwIfDancerIsNotInChat(Set<UUID> dancerIds, Dancer currentDancer) {
        if (!dancerIds.contains(currentDancer.getId())) {
            throw new BusinessException("Current dancer must be part of the chat");
        }
    }

}

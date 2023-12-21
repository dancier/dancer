package net.dancier.dancer.chat;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.chat.client.ChatServiceClient;
import net.dancier.dancer.chat.client.RemoteCreateMessageDto;
import net.dancier.dancer.chat.dto.*;
import net.dancier.dancer.core.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final static Logger log = LoggerFactory.getLogger(ChatService.class);

    private final ChatServiceClient chatServiceClient;

    public ChatDto[] getChatsByUserId(UUID dancerId) {
        log.info("Getting Chats for: " + dancerId);
        return chatServiceClient.getChats(dancerId);
    }

    public CreatedChatDto createChat(UUID dancerId, CreateChatDto createChatDto) {
        log.info("check if dancer is in chat.");
        throwIfDancerIsNotInChat(createChatDto.getParticipantIds(), dancerId);
        log.info("About to make the rest-call");
        return chatServiceClient.createChat(createChatDto);
    }

    public ChatDto getChat(UUID chatId, UUID dancerId) {
        ChatDto chat = chatServiceClient.getChat(chatId);

        throwIfDancerIsNotInChat(chat.getParticipantIds(), dancerId);

        return chat;
    }

    public MessageDto[] getMessages(UUID chatId, UUID dancerId, Optional<UUID> lastMessageId) {
        ChatDto chat = chatServiceClient.getChat(chatId);

        throwIfDancerIsNotInChat(chat.getParticipantIds(), dancerId);

        MessageDto[] messages = chatServiceClient.getMessages(chatId, dancerId, lastMessageId);
        return messages;
    }

    public Void createMessage(UUID chatId, UUID dancerId, CreateMessageDto createMessageDto) {
        ChatDto chat = chatServiceClient.getChat(chatId);

        throwIfDancerIsNotInChat(chat.getParticipantIds(), dancerId);

        RemoteCreateMessageDto remoteCreateMessageDto = new RemoteCreateMessageDto.RemoteCreateMessageDtoBuilder()
                .fromCreateMessageDto(createMessageDto)
                .withAuthorId(dancerId)
                .build();

        return chatServiceClient.createMessage(chatId, remoteCreateMessageDto);
    }

    public Void setReadFlag(UUID messageId, UUID participantId, Boolean read) {
        return chatServiceClient.setReadFlag(messageId, participantId, read);
    }
    private void throwIfDancerIsNotInChat(List<UUID> participantIds, UUID currentDancerId) {
        log.info("Before Check");
        log.info("Part: " + participantIds);
        log.info("Current: " + currentDancerId);
        if (!participantIds.contains(currentDancerId)) {
            throw new BusinessException("Current dancer must be part of the chat");
        }
        log.info("AfterCheck");
    }

}

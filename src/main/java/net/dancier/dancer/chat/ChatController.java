package net.dancier.dancer.chat;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.chat.dto.*;
import net.dancier.dancer.contact.ContactController;
import net.dancier.dancer.core.exception.BusinessException;
import net.dancier.dancer.security.AuthenticatedUser;
import net.dancier.dancer.security.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static net.dancier.dancer.authentication.Constants.ROLE_USER;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private final static Logger log = LoggerFactory.getLogger(ContactController.class);

    private final ChatService chatService;

    @GetMapping("")
    @Secured(ROLE_USER)
    public ResponseEntity<ChatsDto> getChats(@CurrentUser AuthenticatedUser authenticatedUser) {
        log.info("Fetching chats for user {}.", authenticatedUser.getUserId());
        return ResponseEntity.ok(
                chatService.getChatsByUserId(authenticatedUser.getUserId())
        );
    }

    @PostMapping("")
    @Secured(ROLE_USER)
    public ResponseEntity<ChatDto> postChat(
            @CurrentUser AuthenticatedUser authenticatedUser,
            @RequestBody CreateChatDto createChatDto) {
        log.info("Creating a new chat for user {}.", authenticatedUser.getUserId());
        return new ResponseEntity(
                chatService.createChat(authenticatedUser.getUserId(), createChatDto), HttpStatus.CREATED
        );
    }

    @GetMapping("/{chatId}")
    @Secured(ROLE_USER)
    public ResponseEntity<ChatDto> getChat(
            @CurrentUser AuthenticatedUser authenticatedUser,
            @PathVariable UUID chatId) {
        log.info("Fetching single chat {} for user {}.", chatId, authenticatedUser.getUserId());
        return ResponseEntity.ok(
                chatService.getChat(chatId, authenticatedUser.getUserId())
        );
    }

    @GetMapping("/{chatId}/messages")
    @Secured(ROLE_USER)
    public ResponseEntity<MessagesDto> getMessages(
            @CurrentUser AuthenticatedUser authenticatedUser,
            @PathVariable UUID chatId,
            @RequestParam Optional<UUID> lastMessageId) {
        log.info("Fetching messages for chat {} for user {}.", chatId, authenticatedUser.getUserId());
        return ResponseEntity.ok(
                chatService.getMessages(chatId, authenticatedUser.getUserId(), lastMessageId)
        );
    }

    @PostMapping("/{chatId}/messages")
    @Secured(ROLE_USER)
    public ResponseEntity postMessage(
            @CurrentUser AuthenticatedUser authenticatedUser,
            @PathVariable UUID chatId,
            @RequestBody CreateMessageDto createMessageDto) {
        log.info("Creating new message for chat {} for user {}.", chatId, authenticatedUser.getUserId());
        chatService.createMessage(chatId, authenticatedUser.getUserId(), createMessageDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity handle(Throwable throwable) {
        return ResponseEntity.badRequest().build();
    }
}

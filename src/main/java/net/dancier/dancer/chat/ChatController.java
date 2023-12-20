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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.dancier.dancer.authentication.Constants.ROLE_USER;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private final static Logger log = LoggerFactory.getLogger(ChatController.class);

    private final ChatService chatService;

    @GetMapping("")
    @Secured(ROLE_USER)
    public ResponseEntity<List<ChatDto>> getChats(@CurrentUser AuthenticatedUser authenticatedUser) {
        log.info("Fetching chats for user {}.", authenticatedUser.getUserId());
        return ResponseEntity.ok(
                List.of(chatService.getChatsByUserId(authenticatedUser.getDancerIdOrThrow()))
        );
    }

    @PostMapping("")
    @Secured(ROLE_USER)
    public ResponseEntity<CreatedChatDto> postChat(
            @CurrentUser AuthenticatedUser authenticatedUser,
            @RequestBody CreateChatDto createChatDto) {
        log.info("Creating a new chat for User {}.", authenticatedUser.getUserId());

        CreatedChatDto createdChatDto = chatService.createChat(authenticatedUser.getDancerIdOrThrow(), createChatDto);
        log.info("Got this stuff: " + createdChatDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/chats/" + createdChatDto.id)
                .build()
                .toUri();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.set("Location", location.toString());

        return new ResponseEntity(
                createdChatDto,
                headers,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{chatId}")
    @Secured(ROLE_USER)
    public ResponseEntity<ChatDto> getChat(
            @CurrentUser AuthenticatedUser authenticatedUser,
            @PathVariable UUID chatId) {
        log.info("Fetching single chat {} for user {}.", chatId, authenticatedUser.getUserId());
        return ResponseEntity.ok(
                chatService.getChat(chatId, authenticatedUser.getDancerIdOrThrow())
        );
    }

    @GetMapping("/{chatId}/messages")
    @Secured(ROLE_USER)
    public ResponseEntity<MessageDto[]> getMessages(
            @CurrentUser AuthenticatedUser authenticatedUser,
            @PathVariable UUID chatId,
            @RequestParam Optional<UUID> lastMessageId) {
        log.info("Fetching messages for chat {} for user {}.", chatId, authenticatedUser.getUserId());
        return ResponseEntity.ok(
                chatService.getMessages(chatId, authenticatedUser.getDancerIdOrThrow(), lastMessageId)
        );
    }

    @PostMapping("/{chatId}/messages")
    @Secured(ROLE_USER)
    public ResponseEntity postMessage(
            @CurrentUser AuthenticatedUser authenticatedUser,
            @PathVariable UUID chatId,
            @RequestBody CreateMessageDto createMessageDto) {
        log.info("Creating new message for chat {} for user {}.", chatId, authenticatedUser.getUserId());
        chatService.createMessage(chatId, authenticatedUser.getDancerIdOrThrow(), createMessageDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity handle(Throwable throwable) {
        log.info("got this: " + throwable);
        return ResponseEntity.badRequest().build();
    }
}

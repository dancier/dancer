package net.dancier.dancer.chat;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.chat.dto.ChatDto;
import net.dancier.dancer.contact.ContactController;
import net.dancier.dancer.security.AuthenticatedUser;
import net.dancier.dancer.security.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static net.dancier.dancer.authentication.Constants.ROLE_USER;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private final static Logger log = LoggerFactory.getLogger(ContactController.class);

    private final ChatService chatService;

    @GetMapping
    @Secured(ROLE_USER)
    public ResponseEntity<List<ChatDto>> get(@CurrentUser AuthenticatedUser authenticatedUser) {
        log.info("Fetching chats for user {}.", authenticatedUser.getId());
        return ResponseEntity.ok(
                chatService.getChatsByUserId(authenticatedUser.getId())
        );
    }
}

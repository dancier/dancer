package net.dancier.dancer.chat;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.chat.dto.DancerDto;
import net.dancier.dancer.chat.dto.DancerIdsDto;
import net.dancier.dancer.contact.ContactController;
import net.dancier.dancer.core.DancerService;
import net.dancier.dancer.security.AuthenticatedUser;
import net.dancier.dancer.security.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

import static net.dancier.dancer.authentication.Constants.ROLE_USER;

@RestController
@RequestMapping("/dancers")
@RequiredArgsConstructor
public class DancerController {
    private final static Logger log = LoggerFactory.getLogger(ContactController.class);

    private final DancerService dancerService;

    @PostMapping("")
    @Secured(ROLE_USER)
    public ResponseEntity<HashMap<UUID, DancerDto>> post(
            @CurrentUser AuthenticatedUser authenticatedUser,
            @RequestBody DancerIdsDto dancerIdsDto) {
        log.info("Fetching list of dancers for user {}.", authenticatedUser.getUserId());
        return ResponseEntity.ok(
                dancerService.getDancerMap(dancerIdsDto)
        );
    }
}

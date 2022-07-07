package net.dancier.dancer.contact;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.dancier.dancer.authentication.Constants.ROLE_HUMAN;

@RestController
@RequestMapping("contacts")
@RequiredArgsConstructor
public class ContactController {

    private final static Logger log = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;

    @PostMapping
    @Secured(ROLE_HUMAN)
    public ResponseEntity sentMail(@RequestBody ContactDto contactDto) {
        log.info("Sending {} to {}.", contactDto.getMessage(), contactDto.getSender());
        contactService.send(contactDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}

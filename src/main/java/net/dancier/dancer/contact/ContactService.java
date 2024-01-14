package net.dancier.dancer.contact;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.mail.service.MailCreationService;
import net.dancier.dancer.security.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static net.dancier.dancer.authentication.Constants.ROLE_USER;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final static Logger log = LoggerFactory.getLogger(ContactService.class);

    private final MailCreationService mailCreationService;

    private final ApplicationEventPublisher applicationEventPublisher;

    void send(ContactDto contactDto, AuthenticatedUser authenticatedUserOfSender) {
        log.info("Having authenticated: {}", authenticatedUserOfSender);
        String senderMailAddress;
        if (authenticatedUserOfSender.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(s -> s.equals(ROLE_USER))) {
            log.info("Overwriting sender...");
            senderMailAddress = authenticatedUserOfSender.getEmail();
        } else {
            senderMailAddress = contactDto.getSender();
        }
        log.info("Using this: {} as sender address", senderMailAddress);
        SimpleMailMessage mailToSender = mailCreationService.createDancierMessageFromTemplate(
                senderMailAddress,
                "dev@dancier.net",
                "Vielen Dank - Team Dancier",
                MailCreationService.CONTACT_FORMULAR_FEEDBACK,
                Map.of());
        applicationEventPublisher.publishEvent(mailToSender);

        SimpleMailMessage mailToTeamDancier = mailCreationService.createDancierMessageFromTemplate(
                "dev@dancier.net",
                senderMailAddress,
                "Mail über das Kontakt formular",
                MailCreationService.CONTACT_FORMULAR,
                Map.of(
                "sender", senderMailAddress,
                "message", contactDto.getMessage())
                );

        applicationEventPublisher.publishEvent(mailToTeamDancier);
    }

}

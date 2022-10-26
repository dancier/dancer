package net.dancier.dancer.eventlog.controller;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.dto.EventlogMapper;
import net.dancier.dancer.eventlog.dto.NewEventlogDto;
import net.dancier.dancer.eventlog.model.Eventlog;
import net.dancier.dancer.eventlog.service.EventlogService;
import net.dancier.dancer.security.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventlog")
@RequiredArgsConstructor
public class EventlogController {

    private static Logger log = LoggerFactory.getLogger(EventlogController.class);
    private final EventlogService eventlogService;
    @PostMapping
    public ResponseEntity publish(@RequestBody NewEventlogDto newEventlogDto) {
        Eventlog eventlog = EventlogMapper.toEventlog(newEventlogDto);
        setRolesAndUser(eventlog);
        eventlogService.appendNew(eventlog);
        log.info("Appended " + eventlog + " to the eventlog.");
        return ResponseEntity.ok().build();
    }

    private void setRolesAndUser(Eventlog eventlog) {
        switch (SecurityContextHolder.getContext().getAuthentication().getPrincipal()) {
            case AuthenticatedUser authenticatedUser -> {
                eventlog.setUserId(authenticatedUser.getUserId());
                eventlog.setRoles(
                        authenticatedUser
                                .getAuthorities()
                                .stream()
                                .map(Objects::toString)
                                .collect(Collectors.toSet())
                );
            }
            case default -> {
                eventlog.setUserId(null);
                eventlog.setRoles(Set.of("ROLE_ANONYMOUS"));
            }
        }
    }
}

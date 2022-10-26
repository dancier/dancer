package net.dancier.dancer.eventlog.controller;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.dto.EventlogMapper;
import net.dancier.dancer.eventlog.dto.NewEventlogDto;
import net.dancier.dancer.eventlog.service.EventlogService;
import net.dancier.dancer.security.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final EventlogService eventlogService;

    @PostMapping
    public ResponseEntity publish(@RequestBody NewEventlogDto newEventlogDto) {
        newEventlogDto.setUserId(null);
        newEventlogDto.setRoles(Set.of());
        setRolesAndUser(newEventlogDto);
        eventlogService.appendNew(EventlogMapper.toEventlog(newEventlogDto));
        return ResponseEntity.ok().build();
    }

    private void setRolesAndUser(NewEventlogDto newEventlogDto) {
        switch (SecurityContextHolder.getContext().getAuthentication().getPrincipal()) {
            case AuthenticatedUser authenticatedUser -> {
                newEventlogDto.setUserId(authenticatedUser.getUserId());
                newEventlogDto.setRoles(
                        authenticatedUser
                                .getAuthorities()
                                .stream()
                                .map(Objects::toString)
                                .collect(Collectors.toSet())
                );
            }
            case default -> {
                newEventlogDto.setUserId(null);
                newEventlogDto.setRoles(Set.of("ROLE_ANONYMOUS"));
            }
        }
    }
}

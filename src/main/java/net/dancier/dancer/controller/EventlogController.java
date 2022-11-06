package net.dancier.dancer.controller;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.model.EventlogDto;
import net.dancier.dancer.eventlog.service.EventlogService;
import net.dancier.dancer.security.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity publish(@RequestBody EventlogDto eventlogDto) {
        log.info("Got: " + eventlogDto);
         Set<String> roles = Set.of();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication!=null) {
            roles = authentication
                    .getAuthorities()
                    .stream()
                    .map(ga
                            -> (ga.getAuthority()))
                    .collect(Collectors.toSet());
            Object principal = authentication.getPrincipal();
            if (principal!=null && principal instanceof AuthenticatedUser) {
                AuthenticatedUser authenticatedUser = (AuthenticatedUser) principal;
                eventlogDto.setUserId(authenticatedUser.getUserId());
            }
        }
        eventlogDto.setRoles(roles);
        eventlogService.createNew(eventlogDto);
        return ResponseEntity.ok().build();
    }
}

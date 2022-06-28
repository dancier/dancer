package net.dancier.dancer.eventlog;

import net.dancier.dancer.authentication.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventlog")
public class EventlogController {

    private static Logger log = LoggerFactory.getLogger(EventlogController.class);

    @Autowired
    EventlogDAO eventlogDAO;

    @Autowired
    EventlogService eventlogService;
    private Collection<? extends GrantedAuthority> authorities;

    @GetMapping
    public ResponseEntity getCount() {
        return ResponseEntity.ok(eventlogDAO.getCountOfEventlogEntries());
    }

    @PostMapping
    public ResponseEntity publish(@RequestBody EventlogDto eventlogDto) throws SQLException {
        log.info("Got: " + eventlogDto);
         Set<String> roles = Set.of();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication!=null) {
            roles = authentication.getAuthorities().stream().map(ga -> (ga.getAuthority())).collect(Collectors.toSet());
        }
        //authentication.getAuthorities().stream().forEach(e -> roles.add(e.toString()));
        eventlogDto.setRoles(roles);
        eventlogService.createNew(eventlogDto);
        return ResponseEntity.ok().build();
    }
}

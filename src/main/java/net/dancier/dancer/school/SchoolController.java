package net.dancier.dancer.school;

import net.dancier.dancer.security.AuthenticatedUser;
import net.dancier.dancer.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("school")
public class SchoolController {

    @PostMapping
    public ResponseEntity create(@CurrentUser AuthenticatedUser authenticatedUser,
        @RequestBody SchoolDto schoolDto
    ) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{schoolId}")
    public ResponseEntity update(@CurrentUser AuthenticatedUser authenticatedUser,
                                 @RequestBody SchoolDto schoolDto,
                                 @PathVariable UUID schoolId
    ) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{schoolId}")
    public ResponseEntity get(@PathVariable UUID schooId) {
        return ResponseEntity.ok().build();
    }

}

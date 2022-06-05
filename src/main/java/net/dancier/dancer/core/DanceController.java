package net.dancier.dancer.core;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.model.Dance;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static net.dancier.dancer.authentication.Constants.ROLE_USER;

@RestController
@RequestMapping("/dances")
@RequiredArgsConstructor
public class DanceController {

    private final DanceService danceService;

    @Secured(ROLE_USER)
    @GetMapping("/")
    public ResponseEntity getAllDances() {
        return ResponseEntity.ok(danceService
                .getAllDances()
                .stream()
                .map(Dance::getName)
                .collect(Collectors.toList()));
    }

}

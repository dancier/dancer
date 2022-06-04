package net.dancier.dancer.core;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.dto.ProfileDto;
import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.core.model.Dance;
import net.dancier.dancer.security.AuthenticatedUser;
import net.dancier.dancer.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static net.dancier.dancer.authentication.Constants.ROLE_USER;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Secured({ROLE_USER})
    @GetMapping("/checkDancerNameAvailability/{dancerName}")
    public ResponseEntity<?> checkUsernameAvailability(@PathVariable String dancerName) {
        return ResponseEntity.ok(!profileService.existsByDancerName(dancerName));
    }

    @Secured(ROLE_USER)
    @GetMapping
    public ResponseEntity<ProfileDto> get(@CurrentUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(
                profileService.getProfileByUserId(authenticatedUser.getId())
        );
    }

    @Secured(ROLE_USER)
    @GetMapping("/allDances")
    public ResponseEntity getAllDances() {
        return ResponseEntity.ok(profileService
                .getAllDances()
                .stream()
                .map(Dance::getName)
                .collect(Collectors.toList()));
    }

    @Secured(ROLE_USER)
    @PostMapping
    public ResponseEntity put(@CurrentUser AuthenticatedUser authenticatedUser, @RequestBody ProfileDto profileDto) {
        profileService.updateProfileForUserId(authenticatedUser.getId(), profileDto);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity handle(Throwable throwable) {
        return ResponseEntity.notFound().build();
    }
}

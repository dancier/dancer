package net.dancier.dancer.core;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.dto.ProfileDto;
import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.core.model.Dance;
import net.dancier.dancer.security.AuthenticatedUser;
import net.dancier.dancer.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity get(@CurrentUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(
                profileService.getProfileByUserId(authenticatedUser.getId())
        );
    }

    @GetMapping("/allDances")
    public ResponseEntity getAllDances() {
        return ResponseEntity.ok(profileService
                .getAllDances()
                .stream()
                .map(Dance::getName)
                .collect(Collectors.toList()));
    }


    @PostMapping
    public ResponseEntity post(@CurrentUser AuthenticatedUser authenticatedUser, @RequestBody ProfileDto profileDto) {
        profileService.updateProfileForUserId(authenticatedUser.getId(), profileDto);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity handle(Throwable throwable) {
        return ResponseEntity.notFound().build();
    }
}

package net.dancier.dancer.core;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.dto.ProfileDto;
import net.dancier.dancer.core.dto.UsernameAvailableDto;
import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.security.AuthenticatedUser;
import net.dancier.dancer.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static net.dancier.dancer.authentication.Constants.ROLE_USER;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Secured({ROLE_USER})
    @GetMapping("/checkDancerNameAvailability/{dancerName}")
    public ResponseEntity<?> checkUsernameAvailability(@PathVariable String dancerName) {
        return ResponseEntity.ok(
                profileService.existsByDancerName(dancerName) ?
                    UsernameAvailableDto.builder().available(false).name(dancerName).build() :
                    UsernameAvailableDto.builder().available(true).name(dancerName).build()
        );
    }

    @Secured(ROLE_USER)
    @GetMapping
    public ResponseEntity<ProfileDto> get(@CurrentUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(
                profileService.getProfileByUserId(authenticatedUser.getUserId())
        );
    }

    @Secured(ROLE_USER)
    @PutMapping
    public ResponseEntity put(@CurrentUser AuthenticatedUser authenticatedUser, @RequestBody ProfileDto profileDto) {
        profileService.updateProfileForUserId(authenticatedUser.getUserId(), profileDto);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity handle(Throwable throwable) {
        return ResponseEntity.notFound().build();
    }
}

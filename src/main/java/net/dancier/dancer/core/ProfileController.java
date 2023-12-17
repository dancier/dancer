package net.dancier.dancer.core;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.dto.PublicProfileDto;
import net.dancier.dancer.core.dto.ProfileOfCurrentUserDto;
import net.dancier.dancer.core.dto.UsernameAvailableDto;
import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.core.exception.UnresolvableZipCode;
import net.dancier.dancer.security.AuthenticatedUser;
import net.dancier.dancer.security.CurrentUser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Map;
import java.util.UUID;

import static net.dancier.dancer.authentication.Constants.ROLE_USER;

@RestController
@RequestMapping(value = "/profile", produces = {MediaType.APPLICATION_JSON_VALUE})
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
    public ResponseEntity<ProfileOfCurrentUserDto> get(@CurrentUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(
                profileService.getProfileByUserId(authenticatedUser.getUserId())
        );
    }

    @Secured(ROLE_USER)
    @GetMapping("/{dancerId}")
    public ResponseEntity<PublicProfileDto> get(@PathVariable UUID dancerId) {
        PublicProfileDto publicProfileDto = profileService.getProfileByDancerId(dancerId);
        return ResponseEntity.ok(publicProfileDto);
    }

    @Secured(ROLE_USER)
    @PutMapping
    public ResponseEntity put(@CurrentUser AuthenticatedUser authenticatedUser, @Valid @RequestBody ProfileOfCurrentUserDto profileOfCurrentUserDto) {
        profileService.updateProfileForUserId(authenticatedUser.getUserId(), profileOfCurrentUserDto);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({UnresolvableZipCode.class})
    public ResponseEntity handleUnresolvable(Throwable throwable) {
        return ResponseEntity.badRequest().body(Map.of("error","unresolvable zip code"));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity handle(Throwable throwable) {
        return ResponseEntity.notFound().build();
    }
}

package net.dancier.dancer.cabaceo;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.security.AuthenticatedUser;
import net.dancier.dancer.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static net.dancier.dancer.authentication.Constants.ROLE_USER;

@RequestMapping("cabaceos")
@RestController
@RequiredArgsConstructor
public class CabaceoController {

    private final CabaceoService cabaceoService;

    @Secured({ROLE_USER})
    @PostMapping
    public ResponseEntity postNewCabaceo(
            UUID targetDancer,
            String message,
            @CurrentUser AuthenticatedUser authenticatedUser,
            @RequestBody NewCabaceoDto newCabaceoDto
            ) {
        cabaceoService.send(
                authenticatedUser.getId(),
                newCabaceoDto.getTargetDancer(),
                newCabaceoDto.getMessage()
        );
        return ResponseEntity.ok().build();
    }

}

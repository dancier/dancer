package net.dancier.dancer.location;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.dancier.dancer.authentication.Constants.ROLE_USER;

@RestController
@RequestMapping("location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @Secured(ROLE_USER)
    @GetMapping("zipCode/{country}/{code}")
    public ResponseEntity<ZipCode> resolve(@PathVariable String country, @PathVariable String code) {
        ZipCode zipCode = this.locationService.resolveZipCode(country, code);
        if (zipCode == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(zipCode);
    }

}

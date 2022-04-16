package net.dancier.dancer.location;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("zipCode/{country}/{code}")
    public ResponseEntity<ZipCode> resolve(@PathVariable String country, @PathVariable String code) {
        return ResponseEntity.ok(locationService.resolveZipCode(country, code));
    }

}

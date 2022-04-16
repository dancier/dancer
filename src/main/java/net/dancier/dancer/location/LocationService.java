package net.dancier.dancer.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final ZipCodeRepository zipCodeRepository;

    public ZipCode resolveZipCode(String country, String code) {
        return this.zipCodeRepository.findByCountryAndZipCode(country, code);
    }
}

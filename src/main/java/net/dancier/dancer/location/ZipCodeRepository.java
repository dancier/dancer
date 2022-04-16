package net.dancier.dancer.location;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ZipCodeRepository extends CrudRepository<ZipCode, UUID> {
    ZipCode findByCountryAndZipCode(String country, String zipCode);
}

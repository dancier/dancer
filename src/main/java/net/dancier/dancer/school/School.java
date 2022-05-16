package net.dancier.dancer.school;

import lombok.Data;
import net.dancier.dancer.core.model.Country;
import net.dancier.dancer.core.model.Dance;
import net.dancier.dancer.core.model.Recommendable;
import net.dancier.dancer.location.ZipCode;

import java.util.Set;
import java.util.UUID;

@Data
public class School implements Recommendable {

    private UUID id;

    private UUID userId;

    private String name;

    private String url;

    private Set<Dance> supportedDances = Set.of();

    private Country country;

    private ZipCode zipCode;

    private String city;

    private Double latitude;

    private Double longitude;

    private String profileImageHash;

}

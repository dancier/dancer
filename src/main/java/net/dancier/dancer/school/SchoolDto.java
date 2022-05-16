package net.dancier.dancer.school;

import lombok.Data;
import net.dancier.dancer.core.model.Country;

import java.util.Set;

@Data
public class SchoolDto {

    private String name;

    private String url;

    private Set<String> supportedDances;

    private Country country;

    private String city;

    private String zipCode;

    private String profileImageHash;
}

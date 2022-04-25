package net.dancier.dancer.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.dancier.dancer.core.model.Gender;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
public class ProfileDto {

    private UUID id;

    private Integer size;

    private Gender gender;

    private String dancerName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    private Set<DanceProfileDto> ableTo;

    private Set<DanceProfileDto> wantsTo;

    private String email;

    private String zipCode;

    private String city;

    private String country;

    private String profileImageHash;
}

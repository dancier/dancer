package net.dancier.dancer.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.core.model.Gender;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
public class PublicProfileDto {

    public static PublicProfileDto of(Dancer dancer) {
        PublicProfileDto publicProfileDto = new PublicProfileDto();
        publicProfileDto.setId(dancer.getId());
        publicProfileDto.setSize(dancer.getSize());
        publicProfileDto.setGender(dancer.getGender());
        publicProfileDto.setDancerName(dancer.getDancerName());
        publicProfileDto.setBirthDate(dancer.getBirthDate());
        //publicProfileDto.setAbleTo(dancer.getAbleTo());
        //publicProfileDto.setWantsTo(dancer.getWantsTo());
        publicProfileDto.setCity(dancer.getCity());
        //publicProfileDto.setCountry(dancer.getCountry());
        publicProfileDto.setProfileImageHash(dancer.getProfileImageHash());
        publicProfileDto.setAboutMe(dancer.getAboutMe());
        return publicProfileDto;
    }

    private UUID id;

    private Integer size;

    private Gender gender;

    private String dancerName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    private Set<DanceProfileDto> ableTo;

    private Set<DanceProfileDto> wantsTo;

    private String city;

    private String country;

    private String profileImageHash;

    private String aboutMe;
}

package net.dancier.dancer.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.core.model.Gender;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class PublicProfileDto {

    public static PublicProfileDto of(Dancer dancer) {
        PublicProfileDto publicProfileDto = new PublicProfileDto();
        publicProfileDto.setId(dancer.getId());
        publicProfileDto.setSize(dancer.getSize());
        publicProfileDto.setGender(dancer.getGender());
        publicProfileDto.setDancerName(dancer.getDancerName());
        publicProfileDto.setBirthDate(dancer.getBirthDate());
        publicProfileDto.setAbleTo(dancer.getAbleTo().stream()
                .map(dp -> {
                    DanceProfileDto danceProfileDto = new DanceProfileDto();
                    danceProfileDto.setDance(dp.getDance().getName());
                    danceProfileDto.setLeading(dp.getLeading());
                    danceProfileDto.setLevel(dp.getLevel());
                    return danceProfileDto;
                }).collect(Collectors.toSet()));
        publicProfileDto.setWantsTo(dancer.getWantsTo().stream()
                .map(wt -> {
                            DanceProfileDto danceProfileDto = new DanceProfileDto();
                            danceProfileDto.setDance(wt.getDance().getName());
                            danceProfileDto.setLeading(wt.getLeading());
                            danceProfileDto.setLevel(wt.getLevel());
                            return danceProfileDto;
                        }
                ).collect(Collectors.toSet()));
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

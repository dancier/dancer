package net.dancier.dancer.core.dto;

import lombok.Data;
import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.core.model.Gender;

import java.time.LocalDate;
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
        publicProfileDto.setAge(LocalDate.now().getYear() - dancer.getBirthDate().getYear());
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
        publicProfileDto.setCountry(dancer.getCountry().name());
        publicProfileDto.setProfileImageHash(dancer.getProfileImageHash());
        publicProfileDto.setAboutMe(dancer.getAboutMe());
        return publicProfileDto;
    }

    private UUID id;

    private Integer size;

    private Gender gender;

    private String dancerName;

    private Integer age;

    private Set<DanceProfileDto> ableTo;

    private Set<DanceProfileDto> wantsTo;

    private String city;

    private String country;

    private String profileImageHash;

    private String aboutMe;
}

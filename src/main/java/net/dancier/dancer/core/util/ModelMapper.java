package net.dancier.dancer.core.util;

import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.core.dto.DanceProfileDto;
import net.dancier.dancer.core.dto.ProfileDto;
import net.dancier.dancer.core.model.DanceProfile;
import net.dancier.dancer.core.model.Dancer;

import java.util.stream.Collectors;

public class ModelMapper {

    public static ProfileDto dancerAndUserToProfile(Dancer dancer, User user) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setGender(dancer.getGender());
        profileDto.setId(dancer.getId());
        profileDto.setBirthDate(dancer.getBirthDate());
        profileDto.setSize(dancer.getSize());
        profileDto.setZipCode(dancer.getZipCode());
        if (dancer.getCountry()!=null) {
            profileDto.setCountry(dancer.getCountry().name());
        }
        profileDto.setCity(dancer.getCity());
        profileDto.setAbleTo(dancer
                .getAbleTo()
                .stream()
                .map(ModelMapper::danceProfile2danceProfileDto).collect(Collectors.toSet()));
        profileDto.setWantsTo(dancer
                .getWantsTo()
                .stream()
                .map(ModelMapper::danceProfile2danceProfileDto).collect(Collectors.toSet())
        );
        profileDto.setEmail(user.getEmail());
        return profileDto;
    }

    public static DanceProfileDto danceProfile2danceProfileDto(DanceProfile danceProfile) {
        DanceProfileDto danceProfileDto = new DanceProfileDto();
        danceProfileDto.setDance(danceProfile.getDance().getName());
        danceProfileDto.setLeading(danceProfile.getLeading());
        danceProfileDto.setLevel(danceProfile.getLevel());
        return danceProfileDto;
    }

}

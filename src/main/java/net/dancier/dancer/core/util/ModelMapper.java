package net.dancier.dancer.core.util;

import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.core.dto.DanceProfileDto;
import net.dancier.dancer.core.dto.ProfileOfCurrentUserDto;
import net.dancier.dancer.core.model.DanceProfile;
import net.dancier.dancer.core.model.Dancer;

import java.util.stream.Collectors;

public class ModelMapper {

    public static ProfileOfCurrentUserDto dancerAndUserToProfile(Dancer dancer, User user) {
        ProfileOfCurrentUserDto profileOfCurrentUserDto = new ProfileOfCurrentUserDto();
        profileOfCurrentUserDto.setGender(dancer.getGender());
        profileOfCurrentUserDto.setId(dancer.getId());
        profileOfCurrentUserDto.setBirthDate(dancer.getBirthDate());
        profileOfCurrentUserDto.setSize(dancer.getSize());
        profileOfCurrentUserDto.setZipCode(dancer.getZipCode());
        profileOfCurrentUserDto.setDancerName(dancer.getDancerName());
        profileOfCurrentUserDto.setProfileImageHash(dancer.getProfileImageHash());
        if (dancer.getCountry()!=null) {
            profileOfCurrentUserDto.setCountry(dancer.getCountry().name());
        }
        profileOfCurrentUserDto.setCity(dancer.getCity());
        profileOfCurrentUserDto.setAbleTo(dancer
                .getAbleTo()
                .stream()
                .map(ModelMapper::danceProfile2danceProfileDto).collect(Collectors.toSet()));
        profileOfCurrentUserDto.setWantsTo(dancer
                .getWantsTo()
                .stream()
                .map(ModelMapper::danceProfile2danceProfileDto).collect(Collectors.toSet())
        );
        profileOfCurrentUserDto.setEmail(user.getEmail());
        profileOfCurrentUserDto.setAboutMe(dancer.getAboutMe());
        return profileOfCurrentUserDto;
    }

    public static DanceProfileDto danceProfile2danceProfileDto(DanceProfile danceProfile) {
        DanceProfileDto danceProfileDto = new DanceProfileDto();
        danceProfileDto.setDance(danceProfile.getDance().getName());
        danceProfileDto.setLeading(danceProfile.getLeading());
        danceProfileDto.setLevel(danceProfile.getLevel());
        return danceProfileDto;
    }

}

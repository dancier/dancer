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
        profileDto.setSex(dancer.getSex());
        profileDto.setId(dancer.getId());
        profileDto.setSize(dancer.getSize());
        profileDto.setAbleTo(dancer
                .getAbleTo()
                .stream()
                .map(ModelMapper::danceProfile2danceProfile).collect(Collectors.toSet()));
        profileDto.setWantsTo(dancer
                .getWantsTo()
                .stream()
                .map(ModelMapper::danceProfile2danceProfile).collect(Collectors.toSet())
        );
        return profileDto;
    }

    public static DanceProfileDto danceProfile2danceProfile(DanceProfile danceProfile) {
        DanceProfileDto danceProfileDto = new DanceProfileDto();
        danceProfileDto.setDance(danceProfile.getDance().getName());
        danceProfileDto.setLeading(danceProfile.getLeading());
        danceProfileDto.setLevel(danceProfile.getLevel());
        return danceProfileDto;
    }

}

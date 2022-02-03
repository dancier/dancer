package net.dancier.dancer.core.util;

import net.dancier.dancer.core.DanceProfileDto;
import net.dancier.dancer.core.DancerDto;
import net.dancier.dancer.core.model.DanceProfile;
import net.dancier.dancer.core.model.Dancer;

import java.util.stream.Collectors;

public class ModelMapper {
    public static DancerDto dancerToDancerDto(Dancer dancer) {
        DancerDto dancerDto = new DancerDto();
        dancerDto.setSex(dancer.getSex());
        dancerDto.setId(dancer.getId());
        dancerDto.setSize(dancer.getSize());
        dancerDto.setAbleTo(dancer
                .getAbleTo()
                .stream()
                .map(ModelMapper::danceProfile2danceProfile).collect(Collectors.toSet()));
        dancerDto.setWantsTo(dancer
                .getWantsTo()
                .stream()
                .map(ModelMapper::danceProfile2danceProfile).collect(Collectors.toSet())
        );
        return dancerDto;
    }

    public static DanceProfileDto danceProfile2danceProfile(DanceProfile danceProfile) {
        DanceProfileDto danceProfileDto = new DanceProfileDto();
        danceProfileDto.setDance(danceProfile.getDance().getName());
        danceProfileDto.setLeading(danceProfile.getLeading());
        danceProfileDto.setLevel(danceProfile.getLevel());
        return danceProfileDto;
    }

}

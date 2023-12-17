package net.dancier.dancer.core;

import net.dancier.dancer.core.dto.ProfileOfCurrentUserDto;

import java.util.Set;

public class ProfileTestFactory {

    public static ProfileOfCurrentUserDto profileDto() {
        ProfileOfCurrentUserDto profileOfCurrentUserDto = new ProfileOfCurrentUserDto();
        profileOfCurrentUserDto.setWantsTo(Set.of());
        profileOfCurrentUserDto.setAbleTo(Set.of());
        return profileOfCurrentUserDto;
    }
}

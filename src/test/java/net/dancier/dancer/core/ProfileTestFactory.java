package net.dancier.dancer.core;

import net.dancier.dancer.core.dto.ProfileDto;

import java.util.Set;

public class ProfileTestFactory {

    public static ProfileDto profileDto() {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setWantsTo(Set.of());
        profileDto.setAbleTo(Set.of());
        return profileDto;
    }
}

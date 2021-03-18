package net.dancier.service;

import net.dancier.api.Profile;

import java.util.Optional;
import java.util.UUID;

public interface ProfileService {

    Optional<Profile> getProfile(UUID userId);

    void updateProfile(Profile profile);

}

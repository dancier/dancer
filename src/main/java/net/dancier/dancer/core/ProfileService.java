package net.dancier.dancer.core;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.repository.UserRepository;
import net.dancier.dancer.core.dto.ProfileDto;
import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.core.util.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    private final DancerRepository dancerRepository;

    public ProfileDto getProfileByUserId(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found for id: " + userId));
        Dancer dancer = dancerRepository.findByUserId(userId).orElseGet( () -> new Dancer());

        return ModelMapper.dancerAndUserToProfile(dancer, user);
    }

}
package net.dancier.service;

import net.dancier.api.Profile;
import net.dancier.db.DancerDao;
import net.dancier.db.DancerDto;
import net.dancier.db.UserDao;
import net.dancier.domain.User;
import net.dancier.domain.dance.Dancer;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class ProfileServiceImpl implements ProfileService {

    public static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    Jdbi jdbi;
    UserDao userDao;
    DancerDao  dancerDao;

    public ProfileServiceImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.userDao = jdbi.onDemand(UserDao.class);
        this.dancerDao = jdbi.onDemand(DancerDao.class);
    }

    @Override
    public Optional<Profile> getProfile(UUID userId) {
        Optional<User> optionalUser = userDao.lookUpById(userId);
        Optional<DancerDto> optionalDancer = dancerDao.getById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Profile profile = new Profile();
            profile.setUserId(user.getId());
            profile.setEmail(user.getEmail());
            if (optionalDancer.isPresent()) {
                DancerDto dancerDto = optionalDancer.get();
                profile.setAboutHim(dancerDto.getAboutHim());
                profile.setUserName(dancerDto.getUserName());
                profile.setImageId(dancerDto.getImageId());
                profile.setSize(dancerDto.getSize());
                profile.setSmoker(dancerDto.getSmoker());
                profile.setBirthDate(dancerDto.getBirthDate());
            }
            return Optional.of(profile);
        }
        return Optional.empty();
    }

    @Override
    public void updateProfile(Profile profile) {
        DancerDto dancerDto = new DancerDto();
        dancerDto.setAboutHim(profile.getAboutHim());
        dancerDto.setUserName(profile.getUserName());
        // check that this belongs really to the correct user
        dancerDto.setImageId(profile.getImageId());
        dancerDto.setSize(profile.getSize());
        dancerDto.setSmoker(profile.getSmoker());
        dancerDto.setUserId(profile.getUserId());
        dancerDto.setBirthDate(profile.getBirthDate());
        logger.debug("Create or update: " + dancerDto);
        dancerDao.createOrUpdate(dancerDto);
    }
}

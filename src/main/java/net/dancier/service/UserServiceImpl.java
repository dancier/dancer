package net.dancier.service;

import net.dancier.db.UserDao;
import net.dancier.domain.User;
import net.dancier.exception.ConflictingIdSystemException;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private Jdbi jdbi;
    private UserDao userDao;

    public UserServiceImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
        userDao = jdbi.onDemand(UserDao.class);
    }

    @Override
    public User assignUser(User.IdProvider idProvider, String foreignId, String email) throws ConflictingIdSystemException {
        Optional<User> optionalUser = userDao.lookUpByIdProviderAndForeignId(idProvider, foreignId);
        if (optionalUser.isPresent()) {
            logger.debug("Use existing user: " + optionalUser);
            return optionalUser.get();
        }
        logger.debug("Create new user");
        UUID userId = UUID.randomUUID();
        User user = new User(userId, idProvider, foreignId, email);
        userDao.insertUser(user);
        return user;
    }
}

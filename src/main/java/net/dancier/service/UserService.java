package net.dancier.service;

import net.dancier.domain.User;
import net.dancier.exception.ConflictingIdSystemException;

public interface UserService {

    User assignUser(User.IdProvider idProvider, String foreinId, String email) throws ConflictingIdSystemException;
}

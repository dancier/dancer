package net.dancier.dancer.authentication;

import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.core.controller.payload.LoginRequestDto;

public class AuthenticationTestFactory {

    public static RegisterRequestDto registerRequestDto(User user) {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setEmail(user.getEmail());
        registerRequestDto.setPassword(user.getPassword());
        registerRequestDto.setAcceptTermsAndConditions(Boolean.TRUE);
        return registerRequestDto;
    }

    public static LoginRequestDto loginRequestDto(User user) {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setPassword(user.getPassword());
        loginRequestDto.setEmail(user.getEmail());
        return loginRequestDto;
    }

    public static User dummyUser() {
        User user = new User();
        user.setPassword("geheim");
        user.setEmail("max@mustermann.de");
        return user;
    }

}

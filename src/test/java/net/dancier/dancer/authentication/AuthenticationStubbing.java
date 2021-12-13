package net.dancier.dancer.authentication;

import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.User;

public class AuthenticationStubbing {
    public static RegisterRequestDto dummyRegisterRequestDto() {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setEmail("foo@bar.de");
        registerRequestDto.setName("foos");
        registerRequestDto.setUsername("bar");
        registerRequestDto.setPassword("sdfsdf");
        return registerRequestDto;
    }

    public static User dummyUser() {
        User user = new User();
        user.setUsername("bar");
        return user;
    }

}

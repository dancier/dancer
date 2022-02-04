package net.dancier.dancer.core.controller.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}

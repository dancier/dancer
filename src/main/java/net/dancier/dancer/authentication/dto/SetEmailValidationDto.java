package net.dancier.dancer.authentication.dto;

import lombok.Data;

@Data
public class SetEmailValidationDto {

    private String emailAddress;

    private Boolean validated;

}

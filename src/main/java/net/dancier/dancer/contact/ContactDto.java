package net.dancier.dancer.contact;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class ContactDto {

    @Email
    @NotNull
    private String sender;

    @NotNull
    private String message;

}

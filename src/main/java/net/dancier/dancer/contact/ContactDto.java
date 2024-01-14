package net.dancier.dancer.contact;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Data
public class ContactDto {

    @Email
    private String sender;

    @NotNull
    private String message;

}

package net.dancier.domain;

import lombok.Data;

import java.util.UUID;

@Data
public class User {

    private UUID id;
    private String userName;
    private String idSystem;
    private String foreignId;
    private String email;

}

package net.dancier.api;

import lombok.Data;

import java.util.UUID;

@Data
public class Profile {

    private UUID id;

    private String name;
}

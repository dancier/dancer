package net.dancier.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;
    private IdProvider idProvider;
    private String foreignId;
    private String email;

    public enum IdProvider {
        FACEBOOK;
    }
}

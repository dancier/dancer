package net.dancier.dancer.authentication.model;

import lombok.Data;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "password_reset_code")
public class PasswordResetCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID userId;

    private String code;

    private Instant expiresAt;

}

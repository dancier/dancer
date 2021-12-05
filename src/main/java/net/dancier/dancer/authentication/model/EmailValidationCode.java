package net.dancier.dancer.authentication.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name="validation_codes")
public class EmailValidationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    private UUID userId;

    @NotNull
    private String code;

    @NotNull
    private Instant expiresAt;

}

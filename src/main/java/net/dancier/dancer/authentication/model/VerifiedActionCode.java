package net.dancier.dancer.authentication.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name="auth_verified_action_codes")
public class VerifiedActionCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Action action;

    @NotNull
    private String code;

    @NotNull
    private Instant expiresAt;

    public static enum Action {
        LOGIN
    }
}

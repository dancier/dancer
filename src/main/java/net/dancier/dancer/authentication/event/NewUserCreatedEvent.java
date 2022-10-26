package net.dancier.dancer.authentication.event;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class NewUserCreatedEvent {

    private UUID id;

    private String email;

    private Set<String> roles;

    private Boolean isEmailValidated;
}

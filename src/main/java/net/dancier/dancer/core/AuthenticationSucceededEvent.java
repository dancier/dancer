package net.dancier.dancer.core;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.Authentication;

@Builder
@Data
public class AuthenticationSucceededEvent {

    Authentication authentication;

}

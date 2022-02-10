package net.dancier.dancer.security;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface CustomUserDetailsService extends UserDetailsService {
    AuthenticatedUser loadUserById(UUID userId);
}

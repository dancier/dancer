package net.dancier.dancer.security;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public AuthenticatedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));
        return AuthenticatedUser.create(user);
    }
// https://www.netsurfingzone.com/hibernate/failed-to-lazily-initialize-a-collection-of-role-could-not-initialize-proxy-no-session/
    @Transactional
    @Override
    public AuthenticatedUser loadUserById(UUID id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );
        return AuthenticatedUser.create(user);
    }
}
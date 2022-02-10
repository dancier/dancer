package net.dancier.dancer.security;

import lombok.ToString;
import net.dancier.dancer.authentication.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@ToString
public class AuthenticatedUser implements UserDetails {

    private UUID id;

    private String email;

    private String password;

    private boolean isEmailValidated;

    private Collection<? extends GrantedAuthority> authorities;

    public AuthenticatedUser(UUID id,
                             String email,
                             boolean isEmailValidated,
                             String password,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.isEmailValidated = isEmailValidated;
        this.password = password;
        this.authorities = authorities;
    }

    public static AuthenticatedUser create(User user) {
        List<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(
                        role.getName().name())).collect(Collectors.toList());
        return new AuthenticatedUser(
                user.getId(),
                user.getEmail(),
                user.isEmailValidated(),
                user.getPassword(),
                authorities);
    }

    public UUID getId() {
        return id;
    }

    public boolean isEmailValidated() {
        return isEmailValidated;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticatedUser that = (AuthenticatedUser) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
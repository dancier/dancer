package net.dancier.dancer.security;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    private final CustomUserDetailsServiceImpl customUserDetailsService;

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            getJwtFromRequest(request)
                    .ifPresent(token-> {
                        String subject = tokenProvider.getSubjectFromJWT(token);
                        Authentication authentication;
                        switch (subject) {
                            case "HUMAN" : authentication = onlyCaptchaVerified(); break;
                            default: authentication = systemUser(request, subject);
                        }
                        SecurityContextHolder.getContext().setAuthentication(authentication);
            });
        } catch (Exception ex) {
            log.warn("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    private Authentication onlyCaptchaVerified() {

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_HUMAN");
        List<GrantedAuthority> authorities =  List.of(simpleGrantedAuthority);
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                null,
                null,
                true,
                null,
                Optional.empty(),
                authorities
        );


        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_HUMAN");
                return List.of(simpleGrantedAuthority);
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return authenticatedUser;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }

            @Override
            public String getName() {
                return null;
            }
        };
        return authentication;
    }

    private Authentication systemUser(HttpServletRequest httpServletRequest, String subject) {
        AuthenticatedUser authenticatedUser = customUserDetailsService.loadUserById(UUID.fromString(subject));
        if (authenticatedUser.isEmailValidated()) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            return authentication;
        }
        throw new LockedException("E-Mail-Adresse not validated");
    }

    private Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7, bearerToken.length()));
        }
        if (request.getCookies()!=null) {
            for (Cookie cookie: request.getCookies()) {
                if("jwt-token".equals(cookie.getName())) {
                    String jwt = cookie.getValue();
                    if (StringUtils.hasText(jwt)) {
                        return Optional.of (jwt);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        }
        return Optional.empty();
    }}

package net.dancier.dancer.core.config;


import lombok.RequiredArgsConstructor;
import net.dancier.dancer.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                    AuthenticationProvider authenticationProvider,
                                    JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(c -> c.disable())
                .exceptionHandling(Customizer.withDefaults())
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers(
                                        "/authentication/**",
                                        "/eventlog/**"

                                )
                                .permitAll()
                                .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}

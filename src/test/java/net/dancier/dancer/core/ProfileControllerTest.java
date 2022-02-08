package net.dancier.dancer.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.service.AuthenticationService;
import net.dancier.dancer.security.AuthenticatedUser;
import net.dancier.dancer.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfileController.class)
@AutoConfigureMockMvc
public class ProfileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    FilterChainProxy filterChainProxy;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext applicationContext;

    @MockBean
    AuthenticationService authenticationService;

    @Test
    void unauthenticatedRequest_fails() throws Exception {
        mockMvc.perform(get(
                "/profile/"
        )).andExpect(status().isUnauthorized());
    }

    @Configuration
    public static class TestConfig {
        @Bean
        public CustomUserDetailsService customUserDetailsService()
        {
            User user = new User();
            user.setRoles(new HashSet<>());
            AuthenticatedUser authenticatedUser = AuthenticatedUser.create(user);
            CustomUserDetailsService mock = Mockito.mock(CustomUserDetailsService.class);
            when(mock.loadUserById(any())).thenReturn(authenticatedUser);
            when(mock.loadUserByUsername(any())).thenReturn(authenticatedUser);
            return mock;
        }
    }
}

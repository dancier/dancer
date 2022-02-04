package net.dancier.dancer.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.service.AuthenticationService;
import net.dancier.dancer.security.CustomUserDetailsService;
import net.dancier.dancer.security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AuthenticationController.class)
public class DummyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthenticationService authenticationService;

    @Test
    void tmpTest() throws Exception {
    }

    @Configuration
    public static class TestConfig {
        @Bean
        public CustomUserDetailsService customUserDetailsService()
        {
            User user = new User();
            user.setRoles(new HashSet<>());
            UserPrincipal userPrincipal = UserPrincipal.create(user);
            CustomUserDetailsService mock = Mockito.mock(CustomUserDetailsService.class);
            when(mock.loadUserById(any())).thenReturn(userPrincipal);
            when(mock.loadUserByUsername(any())).thenReturn(userPrincipal);
            return mock;
        }
    }
}

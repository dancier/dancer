package net.dancier.dancer.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.service.AuthenticationService;
import net.dancier.dancer.security.CustomUserDetailsService;
import net.dancier.dancer.security.UserPrincipal;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
            user.setUsername("xxx");
            UserPrincipal userPrincipal = UserPrincipal.create(user);
            CustomUserDetailsService mock = Mockito.mock(CustomUserDetailsService.class);
            when(mock.loadUserById(any())).thenReturn(userPrincipal);
            when(mock.loadUserByUsername(any())).thenReturn(userPrincipal);
            return mock;
        }
    }
}

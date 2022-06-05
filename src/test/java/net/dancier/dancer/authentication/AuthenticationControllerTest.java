package net.dancier.dancer.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dancier.dancer.AbstractPostgreSQLEnabledTest;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.service.AuthenticationService;
import net.dancier.dancer.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest extends AbstractPostgreSQLEnabledTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void whenRegisterWithValidInput_thenReturns201() throws Exception {
        User dummyUser = AuthenticationTestFactory.dummyUser();
        RegisterRequestDto registerRequestDto = AuthenticationTestFactory.registerRequestDto(dummyUser);
        mockMvc.perform(
                post("/authentication/registrations")
                        .contentType("application/json")
                        .cookie(getHumanCookie())
                        .content(objectMapper.writeValueAsBytes(registerRequestDto))
        ).andExpect(
                status().isCreated()
        );
    }

    private Cookie getHumanCookie() {
        return new Cookie("jwt-token", jwtTokenProvider.generateJwtToken("HUMAN"));
    }
}
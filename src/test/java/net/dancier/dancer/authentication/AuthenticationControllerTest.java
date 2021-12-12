package net.dancier.dancer.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

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
        when(authenticationService.registerUser(any())).thenReturn(dummyUser());
        mockMvc.perform(
                post("/authentication/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsBytes(dummyRegisterRequestDto()))
        ).andExpect(
                status().isCreated()
        );
    }


    RegisterRequestDto dummyRegisterRequestDto() {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setEmail("foo@bar.de");
        registerRequestDto.setName("foos");
        registerRequestDto.setUsername("bar");
        registerRequestDto.setPassword("sdfsdf");
        return registerRequestDto;
    }

    User dummyUser() {
        User user = new User();
        user.setUsername("bar");
        return user;
    }

}
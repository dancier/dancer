package net.dancier.dancer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String url;

    @BeforeEach
    public void setUp() {
        url = String.format("http://localhost:%d", port);
    }

    public void signUpShould() throws Exception {
        mvc.perform(
                post("/auth/signup")
                        .content(stubSignUpRequest("marci", "marci@gorzala.de"))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isCreated()
        );
    }

    private String stubSignUpRequest(
            String username,
            String email) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new RegisterRequestDto("xxxx", username, email, "xxxxxxx"));
    }
}

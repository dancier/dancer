package net.dancier.dancer;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dancier.dancer.authentication.AuthenticationTestFactory;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.controller.payload.LoginRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class EndToEndTest extends AbstractPostgreSQLEnabledTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TestDatabaseHelper testDatabaseHelper;

    @Test
    void registrationWorks() throws Exception {

        User dummyUser = AuthenticationTestFactory.dummyUser();

        registerUser(dummyUser)
                .andExpect(
                    status().isCreated()
                );

        String emailValidationCode = testDatabaseHelper
                .getEmailValidationCodeForEmail(dummyUser.getEmail());
        assertThat(emailValidationCode).isNotNull();

        loginUser(dummyUser)
                .andExpect(status().isForbidden());

        //validateEmailAddress(emailValidationCode)
        //        .andExpect(status().is4xxClientError());

        // validating the email-adress works
        //// first we invoke the endpoint the user would invoke when he clicks on the link in the mail he got from us.

        //// then we try to login again

        // using
        //String validationCode = getCode();
        //invoke validation endpoint
        // 3)

    }

    private ResultActions validateEmailAddress(String emailValidationCode) throws Exception{
        return mockMvc.perform(
                get("/authentication/email/validate/" + emailValidationCode)
        );
    }

    private ResultActions loginUser(User user) throws Exception {
        LoginRequestDto loginRequestDto = AuthenticationTestFactory.loginRequestDto(user);
        return mockMvc.perform(
                post("/authentication/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsBytes(loginRequestDto))
        );
    }

    private ResultActions registerUser(User user) throws Exception {
        RegisterRequestDto registerRequestDto = AuthenticationTestFactory.registerRequestDto(user);
        return mockMvc.perform(
                post("/authentication/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsBytes(registerRequestDto))
        );
    }
}
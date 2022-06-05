package net.dancier.dancer.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dancier.dancer.AbstractPostgreSQLEnabledTest;
import net.dancier.dancer.TestDatabaseHelper;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.core.controller.payload.LoginRequestDto;
import net.dancier.dancer.mail.service.MailEnqueueService;
import net.dancier.dancer.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EndToEndAuthenticationTest extends AbstractPostgreSQLEnabledTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestDatabaseHelper testDatabaseHelper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MailEnqueueService mailEnqueueService;

    @Test
    void registrationHappyPath() throws Exception {

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

        validateEmailAddress(emailValidationCode)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists("jwt-token"));

        loginUser(dummyUser)
                .andExpect(status().isOk());

    }

    @Test
    void registrationOfAlreadyExistingAccount() throws Exception {

        User dummyUser = AuthenticationTestFactory.dummyUser();

        registerUser(dummyUser)
                .andExpect(
                        status().isCreated()
                );
        String emailValidationCode = testDatabaseHelper.getEmailValidationCodeForEmail(dummyUser.getEmail());
        validateEmailAddress(emailValidationCode);

        // no register again
        registerUser(dummyUser)
                .andExpect(status().isCreated());

        Mockito.verify(mailEnqueueService, times(2)).enqueueMail(any());

    }

    @Test
    public void registrationLostMail() throws Exception {
        User dummyUser = AuthenticationTestFactory.dummyUser();

        registerUser(dummyUser)
                .andExpect(status().isCreated());

        String initialEmailValidationCode = testDatabaseHelper
                .getEmailValidationCodeForEmail(dummyUser.getEmail());
        assertThat(initialEmailValidationCode).isNotNull();

        reRequestEmailValidationCode(dummyUser)
                .andExpect(status().isOk());

        String reRequestedEmailValidationCode = testDatabaseHelper.getEmailValidationCodeForEmail(dummyUser.getEmail());

        assertThat(initialEmailValidationCode).isNotEqualToIgnoringCase(reRequestedEmailValidationCode);

        loginUser(dummyUser)
                .andExpect(status().isForbidden());

        validateEmailAddress(reRequestedEmailValidationCode)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists("jwt-token"));

        loginUser(dummyUser)
                .andExpect(status().isOk());
    }

    private ResultActions reRequestEmailValidationCode(User user) throws Exception {
        return mockMvc.perform(
                post("/authentication/email-validations")
                        .cookie(getHumanCookie())
                        .contentType(MediaType.TEXT_PLAIN).content(user.getEmail())
        );
    }

    private ResultActions validateEmailAddress(String emailValidationCode) throws Exception{
        return mockMvc.perform(
                put("/authentication/email-validations/" + emailValidationCode)
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
                post("/authentication/registrations")
                        .contentType("application/json")
                        .cookie(getHumanCookie())
                        .content(objectMapper.writeValueAsBytes(registerRequestDto))
        );
    }

    private Cookie getHumanCookie() {
        return new Cookie("jwt-token", jwtTokenProvider.generateJwtToken("HUMAN"));
    }

}
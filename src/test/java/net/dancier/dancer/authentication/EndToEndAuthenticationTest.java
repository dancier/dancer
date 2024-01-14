package net.dancier.dancer.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import net.dancier.dancer.AbstractPostgreSQLEnabledTest;
import net.dancier.dancer.TestDatabaseHelper;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.dto.SendLinkDto;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.core.controller.payload.LoginRequestDto;
import net.dancier.dancer.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RecordApplicationEvents
public class EndToEndAuthenticationTest extends AbstractPostgreSQLEnabledTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestDatabaseHelper testDatabaseHelper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    ApplicationEvents applicationEvents;

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

        then(applicationEvents.stream(SimpleMailMessage.class).count()).isEqualTo(2);
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
        SendLinkDto sendLinkDto = new SendLinkDto();
        sendLinkDto.setEmail(user.getEmail());
        return mockMvc.perform(
                post("/authentication/email-validations")
                        .cookie(getHumanCookie())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(sendLinkDto))
        );
    }

    private ResultActions validateEmailAddress(String emailValidationCode) throws Exception {
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
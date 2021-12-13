package net.dancier.dancer;

import net.dancier.dancer.authentication.AuthenticationController;
import net.dancier.dancer.authentication.AuthenticationStubbing;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

public class EndToEndT extends AbstractPostgrSQLEnabledIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void withValidInput_thenAccountCreated() {
        RegisterRequestDto registerRequestDto = AuthenticationStubbing.dummyRegisterRequestDto();

    }
}

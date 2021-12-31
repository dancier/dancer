package net.dancier.dancer;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dancier.dancer.authentication.AuthenticationTestFactory;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

public class EndToEndTest extends AbstractPostgrSQLEnabledTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void withValidInput_thenAccountCreated() {
        RegisterRequestDto registerRequestDto = AuthenticationTestFactory.registerRequestDto();
    }
}

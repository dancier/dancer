package net.dancier.dancer.dancers;

import net.dancier.dancer.AbstractPostgreSQLEnabledTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DancerControllerTest extends AbstractPostgreSQLEnabledTest {


    UUID userId = UUID.fromString("55bbf334-6649-11ed-8f65-5b299f0e161f");

    @Test
    @WithUserDetails("user-with-a-profile@dancier.net")
    void getDancersShouldNotReturnOwnProfile() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/dancers")
                        .param("range", "20")
                        .param("gender", "MALE")
                )
                .andExpect(status().isOk());

    }
}

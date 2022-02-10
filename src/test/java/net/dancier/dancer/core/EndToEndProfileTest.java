package net.dancier.dancer.core;

import net.dancier.dancer.AbstractPostgreSQLEnabledTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class EndToEndProfileTest extends AbstractPostgreSQLEnabledTest {

    @Test
    @WithUserDetails("user@dancier.net")
    void profileOfVirginUser() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/profile"));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.sex").isEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty());

    }

    @Test
    void changeOfBaseAttributes() {

    }

}

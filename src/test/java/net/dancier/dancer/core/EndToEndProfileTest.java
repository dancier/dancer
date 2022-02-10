package net.dancier.dancer.core;

import net.dancier.dancer.AbstractPostgreSQLEnabledTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

public class EndToEndProfileTest extends AbstractPostgreSQLEnabledTest {

    @Test
    @WithUserDetails("user@dancier.net")
    void profileOfVirginUser() {
        System.out.println("");
    }

    @Test
    void changeOfBaseAttributes() {

    }

}

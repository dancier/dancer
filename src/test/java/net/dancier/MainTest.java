package net.dancier;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
public class MainTest {

    private static final  DropwizardAppExtension<DancerConfiguration> EXT = new DropwizardAppExtension<>(
            DancerApplication.class,
            ResourceHelpers.resourceFilePath("config.yml")
    );

    @Test
    public void bla() {
        ObjectMapper objectMapper = EXT.getObjectMapper();
        Assertions.assertNotNull(objectMapper);
    }
}

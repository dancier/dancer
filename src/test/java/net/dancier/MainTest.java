package net.dancier;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class MainTest {

    public static final  DropwizardAppExtension<DancerConfiguration> APP_EXTENSION
            = new DropwizardAppExtension<>(
            DancerApplication.class,
            ResourceHelpers.resourceFilePath("config.yml")
    );


    @Test
    public void testGetRecommendations() {

        Response response = APP_EXTENSION.client().target(
                String.format("http://localhost:%d/recommendations", APP_EXTENSION.getLocalPort()))
                .request()
                .get();

        assertThat(response.getStatus()).isEqualTo(200);

    }
}
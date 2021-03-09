package dancier.net;

import io.dropwizard.Configuration;
import io.dropwizard.client.HttpClientConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookieAuthConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class DancerConfiguration extends Configuration {

    @Valid
    @NotNull
    public  DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    public JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

    @Valid
    @NotNull
    public JwtCookieAuthConfiguration jwtCookieAuth = new JwtCookieAuthConfiguration();

}

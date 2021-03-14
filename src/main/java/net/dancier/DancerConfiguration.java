package net.dancier;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookieAuthConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

    public JwtCookieAuthConfiguration getJwtCookieAuth() {
        return jwtCookieAuth;
    }

    @NotNull
    public LoginConfiguration login = new LoginConfiguration();

    @NotNull
    public CorsConfiguration cors = new CorsConfiguration();
}

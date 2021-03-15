package net.dancier;

import liquibase.pro.packaged.U;
import net.dancier.api.CorsFilter;
import net.dancier.resources.DancerResource;
import net.dancier.resources.DbTestResource;
import net.dancier.resources.UserResource;
import net.dancier.resources.login.LoginResource;
import net.dancier.resources.ProfileResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookieAuthBundle;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.client.Client;

public class DancerApplication extends Application<DancerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DancerApplication().run(args);
    }

    @Override
    public String getName() {
        return "Dancer";
    }

    @Override
    public void initialize(final Bootstrap<DancerConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(JwtCookieAuthBundle.getDefault().withConfigurationSupplier(configuration -> {
            return  ((DancerConfiguration) configuration).jwtCookieAuth;
        }));
        bootstrap.addBundle(new AssetsBundle("/assets/", "/assets/", "index.html"));
        bootstrap.addBundle(new MigrationsBundle<DancerConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(DancerConfiguration dancerConfiguration) {
                return dancerConfiguration.database;
            }
        });
    }

    @Override
    public void run(final DancerConfiguration configuration,
                    final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.database, "postgresql");

        final Client client = new JerseyClientBuilder(environment).using(configuration.jerseyClient).build(getName());
        final CorsFilter corsFilter = new CorsFilter(configuration.cors);

        environment.jersey().register(corsFilter);

        final ProfileResource profileResource = new ProfileResource();
        environment.jersey().register(profileResource);

        final LoginResource loginResource = new LoginResource(client, configuration.login);
        environment.jersey().register(loginResource);

        final DbTestResource dbTestResource = new DbTestResource(jdbi);
        environment.jersey().register(dbTestResource);

        final DancerResource dancerResource = new DancerResource(configuration.database.getUrl(), configuration);
        environment.jersey().register(dancerResource);

        final UserResource userResource = new UserResource(jdbi);
        environment.jersey().register(userResource);
    }

}

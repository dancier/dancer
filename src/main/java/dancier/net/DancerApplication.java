package dancier.net;

import dancier.net.resources.DancerResource;
import dancier.net.resources.DbTestResource;
import dancier.net.resources.LoginResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.apache.http.client.HttpClient;
import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookieAuthBundle;
import org.glassfish.jersey.client.JerseyClient;
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
        bootstrap.addBundle(JwtCookieAuthBundle.getDefault());
        bootstrap.addBundle(new ViewBundle<DancerConfiguration>());
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

        final LoginResource loginResource = new LoginResource(client);
        environment.jersey().register(loginResource);

        final DbTestResource dbTestResource = new DbTestResource(jdbi);
        environment.jersey().register(dbTestResource);

        final DancerResource dancerResource = new DancerResource(configuration.database.getUrl());
        environment.jersey().register(dancerResource);


    }

}

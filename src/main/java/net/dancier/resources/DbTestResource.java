package net.dancier.resources;


import net.dancier.db.MyDao;
import net.dancier.db.UserDao;
import io.dropwizard.auth.Auth;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/user")
@Produces(MediaType.TEXT_HTML)
public class DbTestResource {

    private Jdbi jdbi;
    private MyDao myDao;
    private UserDao userDao;

    public DbTestResource(Jdbi jdbi) {
        this.jdbi = jdbi;
        myDao = jdbi.onDemand(MyDao.class);
        userDao = jdbi.onDemand(UserDao.class);
    }

    @PermitAll
    @GET
    public String healty(@Auth DefaultJwtCookiePrincipal principal) {
        System.out.println("My name: " + principal.getName());
        userDao.insert(UUID.randomUUID(), "facebook", "facid-marc", "ringo-start");
        return jdbi.toString();
    }

}

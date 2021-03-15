package net.dancier.resources;

import net.dancier.db.UserDao;
import net.dancier.domain.User;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserDao userDao;

    public UserResource(Jdbi jdbi) {
        this.userDao = jdbi.onDemand(UserDao.class);
    }

    @GET
    public List<User> getAll() {
        return this.userDao.getAll();
    }
}

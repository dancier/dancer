package net.dancier.resources;

import net.dancier.db.UserDao;
import net.dancier.domain.User;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/user")
public class UserResource {

    public static Logger logger = LoggerFactory.getLogger(UserResource.class);

    private UserDao userDao;

    public UserResource(Jdbi jdbi) {
        this.userDao = jdbi.onDemand(UserDao.class);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAll() {
        return this.userDao.getAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        user.setId(UUID.randomUUID());
        userDao.insertBean(user);
        return Response.status(201).entity(user).build();
    }
}

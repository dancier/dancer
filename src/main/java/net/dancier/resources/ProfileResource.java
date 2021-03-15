package net.dancier.resources;

import io.dropwizard.auth.Auth;
import net.dancier.api.Profile;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
public class ProfileResource {

    @GET
    public Profile getProfile(@Auth DefaultJwtCookiePrincipal principal) {
        Profile profile = new Profile();
        profile.setId(UUID.randomUUID());
        profile.setName("Xiaofei");
        return profile;
    }
}

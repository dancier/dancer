package net.dancier.resources;

import net.dancier.api.Profile;
import io.dropwizard.auth.Auth;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
public class ProfileResource {

    @GET
    public Profile getProfile(@Auth DefaultJwtCookiePrincipal principal, @Context HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies!= null) {
            for (Cookie cookie: cookies
            ) {
                System.out.println(cookie);
            }
        }
        System.out.println("User: " + principal.getName());
        Profile profile = new Profile();
        profile.setId(UUID.randomUUID());
        profile.setName("Xiaofei");
        return profile;
    }
}

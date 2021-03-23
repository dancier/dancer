package net.dancier.resources;

import io.dropwizard.auth.Auth;
import net.dancier.api.Profile;
import net.dancier.service.ProfileService;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
public class ProfileResource {

    public static Logger logger = LoggerFactory.getLogger(ProfileResource.class);

    private ProfileService profileService;

    public ProfileResource(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GET
    public Response getProfile(@Auth DefaultJwtCookiePrincipal principal) {
        Optional<Profile> optionalProfile = profileService.getProfile(UUID.fromString(principal.getName()));
        if (optionalProfile.isPresent()) {
            return Response.ok(optionalProfile.get()).build();
        } else {
            return Response.status(404).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(@Auth DefaultJwtCookiePrincipal principal, Profile profile) {
        if (profile==null) {
            logger.debug("No profile from body");
            profile = new Profile();
        }
        profile.setUserId(UUID.fromString(principal.getName()));
        logger.debug("setting profile: " +  profile);
        profileService.updateProfile(profile);
        return Response.ok().build();
    }

}

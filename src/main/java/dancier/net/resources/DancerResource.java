package dancier.net.resources;

import com.codahale.metrics.annotation.Timed;
import dancier.net.api.Dancer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/dancers")
@Produces(MediaType.APPLICATION_JSON)
public class DancerResource {

    private String name;

    public DancerResource(String name) {
        this.name = name;
    }

    @GET
    @Timed
    public Dancer getAll() {
        Dancer result = new Dancer();
        result.setId(UUID.randomUUID());
        result.setName(name);
        return result;
    }
}

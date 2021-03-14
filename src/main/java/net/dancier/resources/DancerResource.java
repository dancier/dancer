package net.dancier.resources;

import com.codahale.metrics.annotation.Timed;
import net.dancier.DancerConfiguration;
import net.dancier.api.Dancer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/dancers")
@Produces(MediaType.APPLICATION_JSON)
public class DancerResource {

    private String name;
    private DancerConfiguration dancerConfiguration;

    public DancerResource(String name, DancerConfiguration dancerConfiguration) {
        this.name = name;
        this.dancerConfiguration = dancerConfiguration;
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

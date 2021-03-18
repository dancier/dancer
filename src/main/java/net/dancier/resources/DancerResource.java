package net.dancier.resources;

import net.dancier.DancerConfiguration;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/dancers")
@Produces(MediaType.APPLICATION_JSON)
public class DancerResource {

    private String name;
    private DancerConfiguration dancerConfiguration;

    public DancerResource(String name, DancerConfiguration dancerConfiguration) {
        this.name = name;
        this.dancerConfiguration = dancerConfiguration;
    }

}

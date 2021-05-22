package net.dancier.resources;

import net.dancier.service.RecommendationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/recommendations")
public class RecommendationsResource {

    public static Logger logger = LoggerFactory.getLogger(RecommendationsResource.class);

    private RecommendationsService recommendationsService;

    public RecommendationsResource(RecommendationsService recommendationsService) {
        this.recommendationsService = recommendationsService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIt() {
        return Response.ok(recommendationsService.get(null)).build();
    }
}

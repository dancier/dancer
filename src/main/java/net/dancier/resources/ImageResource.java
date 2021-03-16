package net.dancier.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

@Path("/image")
public class ImageResource {

    public static Logger logger = LoggerFactory.getLogger(ImageResource.class);

    @GET
    @Path("/{imageId}.{type}")
    @Produces("image/png")
    public Response getImage(
            @PathParam("type") String type,
            @PathParam("imageId") String id
    ) {
        logger.debug("About to return an image." + id);
        logger.debug("With type: "+  type);
        File file = new File("/data/images/" + id + ".png");
        return returnFile(file);
    }

    public static Response returnFile(File file) {
        if (!file.exists()) {
            logger.debug("Did not found the image. Returning 404.");
            logger.debug(file.getAbsolutePath());
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            logger.debug("Yes");
            Date fileDate = new Date(file.lastModified());
            return Response.ok(new FileInputStream(file)).lastModified(fileDate).build();
        } catch (FileNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}

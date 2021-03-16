package net.dancier.resources;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Date;
import java.util.UUID;

@Path("/image")
public class ImageResource {

    public static Logger logger = LoggerFactory.getLogger(ImageResource.class);
    public static final String UPLOAD_PATH = "/data/images/";

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileMetaData
    ) {
        logger.debug("receiving file");
        UUID fileId = UUID.randomUUID();
        try
        {
            int read = 0;
            byte[] bytes = new byte[1024];

            OutputStream out = new FileOutputStream(new File(UPLOAD_PATH + fileId.toString()));
            while ((read = fileInputStream.read(bytes)) != -1)
            {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e)
        {
            throw new WebApplicationException("Error while uploading file. Please try again !!");
        }
        return Response.ok(fileId).build();
    }

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

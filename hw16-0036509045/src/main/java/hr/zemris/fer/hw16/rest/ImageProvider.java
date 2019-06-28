package hr.zemris.fer.hw16.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hr.zemris.fer.hw16.models.ImageModel;
import hr.zemris.fer.hw16.repositories.RepositoryProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Rest service for providing information about images.
 *
 * @author Jan Capek
 */
@Path("/image")
public class ImageProvider {

    /**
     * Creates a json response which is an array containing all available tags as strings.
     *
     * @return Json response.
     */
    @GET
    @Path("tags")
    @Produces("application/json")
    public Response getAvailableTags() {
        Gson gson = new Gson();
        String result = gson.toJson(RepositoryProvider.getImageRepository().getAvailableTags());
        return Response.status(Response.Status.OK).entity(result).build();
    }

    /**
     * Creates a json response which is an array of {@link ImageModel} objects converted to JSON.
     * Array will consist of images which have given tag.
     *
     * @param tagName Name of the tag which returned images should have.
     * @return Json response.
     */
    @GET
    @Path("tags/{tagName}")
    @Produces("application/json")
    public Response getThumbnailsForTag(@PathParam("tagName") String tagName) {
        Gson gson = new Gson();
        String result = gson.toJson(RepositoryProvider.getImageRepository().findWithTag(tagName));
        return Response.status(Response.Status.OK).entity(result).build();
    }

    /**
     * Creates a json response which is a {@link ImageModel} object converted to JSON.
     * Returned image will be the one with given name.<br>
     * If wanted image is not found json with field 'error' with appropriate message and status code 404 will be returned.
     *
     * @param imageName Name of the image which info is expected.
     * @return Json response.
     */
    @GET
    @Path("{imageName}")
    @Produces("application/json")
    public Response getImageInfo(@PathParam("imageName") String imageName) {
        Gson gson = new Gson();
        ImageModel img = RepositoryProvider.getImageRepository().findByName(imageName);
        Response.Status responseStatus;
        String result;
        if (img == null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", "Image not found.");
            result = gson.toJson(jsonObject);
            responseStatus = Response.Status.NOT_FOUND;
        } else {
            result = gson.toJson(img);
            responseStatus = Response.Status.OK;
        }
        return Response.status(responseStatus).entity(result).build();
    }
}

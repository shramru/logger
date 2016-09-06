package webhandlers;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by vladislav on 06.09.16.
 */
@Singleton
@Path("/")
public class WebHandler {

    @GET
    public Response log() {
        return Response.ok().build();
    }
}

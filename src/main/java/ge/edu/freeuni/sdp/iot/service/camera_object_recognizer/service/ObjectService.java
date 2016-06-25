package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/houses/{house_id}/objects")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ObjectService {

    @GET
    public Response stub() {
        return Response.ok().build();
    }

}

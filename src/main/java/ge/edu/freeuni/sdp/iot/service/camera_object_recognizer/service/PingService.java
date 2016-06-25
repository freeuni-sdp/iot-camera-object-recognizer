package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("ping")
public class PingService {

    @GET
    public Response ping() {
        return Response.ok().build();
    }
}

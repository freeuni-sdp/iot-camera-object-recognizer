package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy;

import javax.ws.rs.core.Response;

public final class ResponseUtils {

    public static boolean is200(Response response) {
        return isStatus(response, Response.Status.OK);
    }

    public static boolean is201(Response response) {
        return isStatus(response, Response.Status.CREATED);
    }

    public static boolean is404(Response response) {
        return isStatus(response, Response.Status.NOT_FOUND);
    }

    private static boolean isStatus(Response response, Response.Status status) {
        return response.getStatus() == status.getStatusCode();
    }
}

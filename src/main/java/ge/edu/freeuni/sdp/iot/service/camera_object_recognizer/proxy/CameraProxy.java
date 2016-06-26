package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy;

import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ServiceState;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CameraProxy {

    private static final String REAL_CAMERA_API =
            "";
    private static final String DEV_CAMERA_API =
            "https://iot-garden-simulator.herokuapp.com/webapi";

    private final Client client;
    private final ServiceState state;

    public CameraProxy(ServiceState state) {
        this.state = state;
        ClientConfig config = new ClientConfig().register(JacksonFeature.class);
        this.client = ClientBuilder.newClient(config);
    }

    private String getApiUrl() {
        String apiUrl= null;

        if (state == ServiceState.DEV)
            apiUrl = DEV_CAMERA_API;
        else if (state == ServiceState.REAL)
            apiUrl = REAL_CAMERA_API;

        return apiUrl;
    }

    public byte[] get(String houseId) {
        String requestUrl = String.format("%s/houses/%s/camera", getApiUrl(), houseId);
        Response response = client
                .target(requestUrl)
                .request(MediaType.APPLICATION_OCTET_STREAM)
                .get();

        byte[] ret = null;

        if (ResponseUtils.is200(response))
            ret = response.readEntity(byte[].class);

        return ret;
    }
}

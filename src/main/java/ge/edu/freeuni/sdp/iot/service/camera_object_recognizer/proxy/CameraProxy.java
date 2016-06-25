package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy;

import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ServiceState;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class CameraProxy {

    // TODO set values
    private static final String REAL_CAMERA_API =
            "";
    private static final String DEV_CAMERA_API =
            "";

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
        // TODO add real implemetation
        return new byte[0];
    }
}

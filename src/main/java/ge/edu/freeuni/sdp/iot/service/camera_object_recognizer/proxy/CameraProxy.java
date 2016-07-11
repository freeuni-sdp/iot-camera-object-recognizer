package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy;

import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ServiceState;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CameraProxy {

    private final Client client;
    private final ServiceState state;

    public CameraProxy(ServiceState state) {
        this.state = state;
        ClientConfig config = new ClientConfig().register(JacksonFeature.class);
        this.client = ClientBuilder.newClient(config);
    }

    public byte[] get(String cameraUrl) {
        Response response = client
                .target(cameraUrl)
                .request(MediaType.APPLICATION_OCTET_STREAM)
                .get();

        byte[] ret = null;

        if (ResponseUtils.is200(response))
            ret = response.readEntity(byte[].class);

        return ret;
    }
}

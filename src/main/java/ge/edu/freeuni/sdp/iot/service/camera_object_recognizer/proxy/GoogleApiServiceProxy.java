package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy;

import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ServiceState;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by misho on 6/25/16.
 */
public class GoogleApiServiceProxy {

    private static final String REAL_GOOGLE_API =
            "";
    private static final String DEV_GOOGLE_API =
            "";

    private final Client client;
    private final ServiceState state;

    public GoogleApiServiceProxy(ServiceState state) {
        this.state = state;
        ClientConfig config = new ClientConfig().register(JacksonFeature.class);
        this.client = ClientBuilder.newClient(config);
    }

    public List<String> getObjectList(byte[] img) {
        return new ArrayList<String>();
    }
}

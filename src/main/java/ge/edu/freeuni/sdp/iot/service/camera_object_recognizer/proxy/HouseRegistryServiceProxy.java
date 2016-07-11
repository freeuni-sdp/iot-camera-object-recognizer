package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy;

import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ServiceState;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class HouseRegistryServiceProxy {

    private static final String REAL_HOUSE_REGISTRY_API =
            "https://iot-house-registry.herokuapp.com";
    private static final String DEV_HOUSE_REGISTRY_API =
            "http://private-3aa89-iothouseregistry.apiary-mock.com";

    private static final String KEY_CAMERA = "cam_ip";
    private static final String KEY_SUB_IP = "_";

    private final Client client;
    private final ServiceState state;

    public HouseRegistryServiceProxy(ServiceState state) {
        this.state = state;
        ClientConfig config = new ClientConfig().register(JacksonFeature.class);
        this.client = ClientBuilder.newClient(config);
    }

    private String getApiUrl() {
        String apiUrl= null;

        if (state == ServiceState.DEV)
            apiUrl = DEV_HOUSE_REGISTRY_API;
        else if (state == ServiceState.REAL)
            apiUrl = REAL_HOUSE_REGISTRY_API;

        return apiUrl;
    }

    public boolean get(String houseId) {
        String requestUrl = String.format("%s/houses/%s", getApiUrl(), houseId);
        Response response = client
                .target(requestUrl)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        return ResponseUtils.is200(response);
    }

    public String getCameraUrl(String houseId) {
        String requestUrl = String.format("%s/houses/%s", getApiUrl(), houseId);
        Response response = client
                .target(requestUrl)
                .request(MediaType.APPLICATION_OCTET_STREAM)
                .get();
        try {
            JSONObject obj =  new JSONObject(response.readEntity(String.class));
            return obj.getJSONObject(KEY_CAMERA)
                    .getString(KEY_SUB_IP);
        } catch (Exception ignored){}
        return null;
    }
}

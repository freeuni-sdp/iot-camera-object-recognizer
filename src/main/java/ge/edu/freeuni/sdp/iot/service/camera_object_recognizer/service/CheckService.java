package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import com.microsoft.azure.storage.StorageException;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ObjectEntity;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy.ProxyFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("/houses/{house_id}/check")
@Produces(MediaType.APPLICATION_JSON)
public class CheckService extends Services {

    private static final int DEFAULT_MAX_RESULTS = 50;

    @GET
    public CheckDo checkForUnknownObjects(@PathParam("house_id") String houseId) throws StorageException, IOException, GeneralSecurityException {
        if (!houseExists(houseId))
            throw new NotFoundException();

        Map<String, Integer> knowns = new HashMap<>();
        int numObjects = 0;
        for (ObjectEntity obj : getRepository ().getAll(houseId)) {
            String type = obj.toDo().getType();
            int quantity = 1;
            if (knowns.containsKey(type))
                quantity = knowns.get(type) + 1;
            knowns.put(type, quantity);
            numObjects++;
        }

        List<String> unkowns = new ArrayList<>();
        ProxyFactory factory = getProxyFactory();
        String cameraUrl = factory.getHouseRegistryService().getCameraUrl(houseId);

        if (cameraUrl == null)
            throw new NotFoundException();

        List<String> found = factory.getGoogleApiProxy().getObjectList(
                factory.getCamera().get(cameraUrl), DEFAULT_MAX_RESULTS);
        for (String type : found) {
            if (knowns.containsKey(type)) {
                int count = knowns.get(type) - 1;
                if (count == 0)
                    knowns.remove(type);
                else
                    knowns.put(type, count);
            } else {
                unkowns.add(type);
            }
        }
        return new CheckDo(unkowns.size() == 0, unkowns);
    }
}

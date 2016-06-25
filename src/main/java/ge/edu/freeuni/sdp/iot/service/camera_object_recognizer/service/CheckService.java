package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import com.microsoft.azure.storage.StorageException;
import com.sun.javafx.collections.MappingChange;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data.Repository;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data.RepositoryFactory;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ObjectEntity;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy.ProxyFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by misho on 6/25/16.
 */
@Path("/houses/{house_id}/check")
@Produces(MediaType.APPLICATION_JSON)
public class CheckService extends Services {

    @GET
    public CheckDo checkForUnknownObjects(@PathParam("house_id") String houseId) throws StorageException {
        if (!houseExists(houseId))
            throw new NotFoundException();
        Map<String, Integer> knowns = new HashMap<>();
        for (ObjectEntity obj : getRepository ().getAll(houseId)) {
            String type = obj.toDo().getType();
            int quantity = 1;
            if (knowns.containsKey(type))
                quantity = knowns.get(type) + 1;
            knowns.put(type, quantity);
        }
        List<String> unkowns = new ArrayList<>();
        ProxyFactory factory = getProxyFactory();
        List<String> found = factory.getGoogleApiProxy().getObjectList(factory.getCamera().get(houseId));
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

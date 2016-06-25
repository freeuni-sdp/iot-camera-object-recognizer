package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import com.microsoft.azure.storage.StorageException;
import com.sun.javafx.collections.MappingChange;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data.Repository;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data.RepositoryFactory;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ObjectEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
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

        }
        return new CheckDo(true, new ArrayList<String>());
    }
}

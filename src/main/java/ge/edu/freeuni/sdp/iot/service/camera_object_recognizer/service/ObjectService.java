package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import com.microsoft.azure.storage.StorageException;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data.Repository;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data.RepositoryFactory;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ObjectEntity;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy.ProxyFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/houses/{house_id}/objects")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ObjectService {

    public Repository getRepository() throws StorageException {
        return RepositoryFactory.create();
    }

    public ProxyFactory getProxyFactory() {
        return ProxyFactory.getProxyFactory();
    }

    @GET
    public List<ObjectDo> stub(@PathParam("house_id") String houseId) throws StorageException {

        if (!houseExists(houseId))
            throw new NotFoundException();
        List<ObjectDo> result = new ArrayList<>();
        for (ObjectEntity obj : getRepository().getAll()) {
            result.add(obj.toDo());
        }
        return result;
    }

    @POST
    public ObjectDo addObject(@PathParam("house_id") String houseId, ObjectDo obj) throws StorageException {
        if (!houseExists(houseId) || obj.getType() == null)
            throw new NotFoundException();
        obj.setId(UUID.randomUUID().toString());
        try {
            getRepository().insertOrUpdate(ObjectEntity.fromDo(obj));
        } catch (StorageException e) {
            throw new InternalServerErrorException();
        }
        return obj;
    }

    @GET
    @Path("{object_id}")
    public ObjectDo getObjectById(@PathParam("house_id") String houseId,
                                  @PathParam("object_id") String objectId) throws StorageException {
        ObjectEntity entity = getRepository().find(objectId);
        if (!houseExists(houseId) || entity == null)
            throw new NotFoundException();
        return entity.toDo();
    }

    private boolean houseExists(String id) {
        return getProxyFactory()
                .getHouseRegistryService()
                .get(id);
    }
}

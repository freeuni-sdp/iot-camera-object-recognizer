package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data;

import com.microsoft.azure.storage.StorageException;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ObjectEntity;

public interface Repository {

    void insertOrUpdate(ObjectEntity object) throws StorageException;

    ObjectEntity delete(String houseId, String id) throws StorageException;

    ObjectEntity find(String houseId, String id) throws StorageException;

    Iterable<ObjectEntity> getAll(String houseId);
}

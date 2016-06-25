package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data;

import com.microsoft.azure.storage.StorageException;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ObjectEntity;

public interface Repository {

    void insertOrUpdate(ObjectEntity task) throws StorageException;

    ObjectEntity delete(String id) throws StorageException;

    ObjectEntity find(String id) throws StorageException;

    Iterable<ObjectEntity> getAll();
}

package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ObjectEntity;

public class CloudRepository implements Repository {

    private CloudTable table;

    public CloudRepository(CloudTable table) {
        this.table = table;
    }

    @Override
    public void insertOrUpdate(ObjectEntity task) throws StorageException {

    }


    @Override
    public ObjectEntity delete(String id) throws StorageException {
        return null;
    }

    @Override
    public ObjectEntity find(String id) throws StorageException {
        return null;
    }

    @Override
    public Iterable<ObjectEntity> getAll() {
        return null;
    }
}

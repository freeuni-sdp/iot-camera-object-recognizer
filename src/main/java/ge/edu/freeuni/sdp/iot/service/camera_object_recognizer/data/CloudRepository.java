package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ObjectEntity;

public class CloudRepository implements Repository {

    private CloudTable table;

    public CloudRepository(CloudTable table) {
        this.table = table;
    }

    @Override
    public void insertOrUpdate(ObjectEntity object) throws StorageException {
        table.execute(TableOperation.insertOrReplace(object));
    }


    @Override
    public ObjectEntity delete(String houseId, String objectId) throws StorageException {
        ObjectEntity object = find(houseId, objectId);
        if (object != null) {
            TableOperation operation = TableOperation.delete(object);
            table.execute(operation);
        }

        return object;
    }

    @Override
    public ObjectEntity find(String houseId, String objectId) throws StorageException {
        TableOperation operation = TableOperation.retrieve(houseId, objectId, ObjectEntity.class);

        return table.execute(operation).getResultAsType();
    }

    @Override
    public Iterable<ObjectEntity> getAll() {
        return table.execute(TableQuery.from(ObjectEntity.class));
    }
}

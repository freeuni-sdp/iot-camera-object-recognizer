package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ObjectEntity;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ObjectEntityId;

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
    public ObjectEntity delete(String id) throws StorageException {
        ObjectEntity object = find(id);
        if (object != null) {
            TableOperation operation = TableOperation.delete(object);
            table.execute(operation);
        }

        return object;
    }

    @Override
    public ObjectEntity find(String id) throws StorageException {
        ObjectEntityId objectEntityId = new ObjectEntityId(id);
        TableOperation operation = TableOperation.retrieve(
                objectEntityId.getPartitionKey(), objectEntityId.getRowKey(), ObjectEntity.class);

        return table.execute(operation).getResultAsType();
    }

    @Override
    public Iterable<ObjectEntity> getAll() {
        return table.execute(TableQuery.from(ObjectEntity.class));
    }
}

package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data;

import com.microsoft.azure.storage.StorageException;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ObjectEntity;

import java.util.Collections;
import java.util.HashMap;

public class FakeRepository implements Repository {

    private static FakeRepository instance;
    private final HashMap<String, HashMap<String, ObjectEntity>> memo;

    public FakeRepository(HashMap<String, HashMap<String, ObjectEntity>> memo) {
        this.memo = memo;
    }

    public static FakeRepository instance() {
        if (instance==null) {
            instance = new FakeRepository(new HashMap<String, HashMap<String, ObjectEntity>>());
        }
        return instance;
    }

    @Override
    public void insertOrUpdate(ObjectEntity object) throws StorageException {
        HashMap<String, ObjectEntity> partition = new HashMap<>();
        if (memo.containsKey(object.getPartitionKey()))
            partition = memo.get(object.getPartitionKey());
        partition.put(object.getRowKey(), object);
        memo.put(object.getPartitionKey(), partition);
    }

    @Override
    public ObjectEntity delete(String houseId, String id) throws StorageException {
        ObjectEntity object = null;
        if (memo.containsKey(houseId)) {
            HashMap<String, ObjectEntity> partition = memo.get(houseId);
            if (partition.containsKey(id))
                object = partition.remove(id);
        }
        return object;
    }

    @Override
    public ObjectEntity find(String houseId, String id) throws StorageException {
        ObjectEntity object = null;
        if (memo.containsKey(houseId)) {
            HashMap<String, ObjectEntity> partition = memo.get(houseId);
            if (partition.containsKey(id))
                object = partition.get(id);
        }
        return object;
    }

    @Override
    public Iterable<ObjectEntity> getAll(String houseId) {
        Iterable<ObjectEntity> objects = Collections.emptyList();
        if (memo.containsKey(houseId))
            objects = memo.get(houseId).values();
        return objects;
    }

    public void clear() {
        memo.clear();
    }

    public boolean contains(String houseId, String id) {
        return memo.containsKey(houseId) && memo.get(houseId).containsKey(id);
    }
}

package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data;

import com.microsoft.azure.storage.StorageException;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ObjectEntity;

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
    public void insertOrUpdate(ObjectEntity task) throws StorageException {

    }

    @Override
    public ObjectEntity delete(String houseId, String id) throws StorageException {
        return null;
    }

    @Override
    public ObjectEntity find(String houseId, String id) throws StorageException {
        return null;
    }

    @Override
    public Iterable<ObjectEntity> getAll(String houseId) {
        return null;
    }

    public void clear() {
        memo.clear();
    }
}

package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model;

import com.microsoft.azure.storage.table.TableServiceEntity;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service.ObjectDo;

public class ObjectEntity extends TableServiceEntity {

    private String type;

    public ObjectEntity() {
    }

    public ObjectEntity(ObjectDo objectDo) {
        ObjectEntityId entityId = new ObjectEntityId(objectDo.getId());
        this.partitionKey = entityId.getPartitionKey();
        this.rowKey = entityId.getRowKey();
        this.type = objectDo.getType();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ObjectDo toDo() {
        String id = new ObjectEntityId(partitionKey, rowKey).getId();
        return new ObjectDo(id, type);
    }

    public static ObjectEntity fromDo(ObjectDo objectDo) {
        return new ObjectEntity(objectDo);
    }
}

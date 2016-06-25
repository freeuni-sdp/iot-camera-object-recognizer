package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model;

import com.microsoft.azure.storage.table.TableServiceEntity;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service.ObjectDo;

public class ObjectEntity extends TableServiceEntity {

    private String type;

    public ObjectEntity() {
    }

    private ObjectEntity(String houseId, ObjectDo objectDo) {
        this.partitionKey = houseId;
        this.rowKey = objectDo.getId();
        this.type = objectDo.getType();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ObjectDo toDo() {
        return new ObjectDo(getRowKey(), type);
    }

    public static ObjectEntity fromDo(String houseId, ObjectDo objectDo) {
        return new ObjectEntity(houseId, objectDo);
    }
}

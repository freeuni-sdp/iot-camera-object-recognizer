package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model;

public class ObjectEntityId {

    private String partitionKey;

    private String rowKey;

    public ObjectEntityId(String id) {
        this.partitionKey = id.substring(0, 3);
        this.rowKey = id.substring(3);
    }

    public ObjectEntityId(String partitionKey, String rowKey) {
        this.partitionKey = partitionKey;
        this.rowKey = rowKey;
    }

    public String getPartitionKey() {
        return partitionKey;
    }

    public String getRowKey() {
        return rowKey;
    }

    public String getId() {
        return partitionKey + rowKey;
    }
}

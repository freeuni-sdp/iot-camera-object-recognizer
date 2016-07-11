package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectDo {

    @JsonProperty(required = false)
    private String id;

    private String type;

    public ObjectDo() {
    }

    public ObjectDo(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectDo objectDo = (ObjectDo) o;

        if (id != null ? !id.equals(objectDo.id) : objectDo.id != null) return false;
        return type != null ? type.equals(objectDo.type) : objectDo.type == null;
    }
}

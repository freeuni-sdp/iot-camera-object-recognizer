package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckDo {

    @JsonProperty(required = true)
    private boolean result;

    @JsonProperty(required = false)
    private List<String> objects;

    public CheckDo(){}

    public CheckDo(boolean result, List<String> objects) {
        this.result = result;
        this.objects = objects;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<String> getObjects() {
        return objects;
    }

    public void setObjects(List<String> objects) {
        this.objects = objects;
    }
}

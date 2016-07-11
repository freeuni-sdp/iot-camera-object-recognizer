package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import java.util.List;


public class CheckDo {

    private boolean result;

    private List<String> objects;

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

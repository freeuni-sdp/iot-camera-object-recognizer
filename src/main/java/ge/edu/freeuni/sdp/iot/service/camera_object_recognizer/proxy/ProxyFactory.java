package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy;


import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ServiceState;

public class ProxyFactory {

    protected ServiceState getServiceState() {
        return ServiceState.DEV;
    }

    public CameraProxy getCamera() {
        return new CameraProxy(getServiceState());
    }
}

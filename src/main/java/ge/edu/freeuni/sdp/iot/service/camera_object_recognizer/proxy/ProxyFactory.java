package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy;


import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ServiceState;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ProxyFactory {

    protected ServiceState getServiceState() {
        return ServiceState.DEV;
    }

    public static ProxyFactory getProxyFactory() {
        return new ProxyFactory();
    }

    public CameraProxy getCamera() {
        return new CameraProxy(getServiceState());
    }

    public GoogleApiServiceProxy getGoogleApiProxy() {
        return new GoogleApiServiceProxy(getServiceState());
    }

    public HouseRegistryServiceProxy getHouseRegistryService() {
        return new HouseRegistryServiceProxy(getServiceState());
    }
}

package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy;

import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ServiceState;

/**
 * Created by misho on 7/11/16.
 */
public class FakeProxyFactory extends ProxyFactory {

    private static FakeProxyFactory proxyFactory;

    public static FakeProxyFactory getFakeFactory(){
        if (proxyFactory == null)
            proxyFactory = new FakeProxyFactory();
        return proxyFactory;
    }

    private CameraProxy camera;
    private GoogleApiServiceProxy googleApiService;
    private HouseRegistryServiceProxy houseRegistryService;


    public void setHouseRegistryService(HouseRegistryServiceProxy houseRegistryService) {
        this.houseRegistryService = houseRegistryService;
    }

    public void setGoogleApiService(GoogleApiServiceProxy googleApiService) {
        this.googleApiService = googleApiService;
    }

    public void setCamera(CameraProxy camera) {
        this.camera = camera;
    }

    @Override
    public CameraProxy getCamera() {
        return this.camera;
    }

    @Override
    public GoogleApiServiceProxy getGoogleApiProxy() {
        return this.googleApiService;
    }

    @Override
    public HouseRegistryServiceProxy getHouseRegistryService() {
        return this.houseRegistryService;
    }
}

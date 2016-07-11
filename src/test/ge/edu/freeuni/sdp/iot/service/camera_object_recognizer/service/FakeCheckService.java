package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import com.microsoft.azure.storage.StorageException;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data.Repository;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy.ProxyFactory;

/**
 * Created by misho on 7/11/16.
 */
public class FakeCheckService extends CheckService {
    @Override
    public Repository getRepository() throws StorageException {
        return super.getRepository();
    }

    @Override
    public ProxyFactory getProxyFactory() {
        return super.getProxyFactory();
    }

}
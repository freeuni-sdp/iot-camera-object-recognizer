package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import com.microsoft.azure.storage.StorageException;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data.FakeRepository;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data.Repository;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy.FakeProxyFactory;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy.ProxyFactory;

public class FakeObjectService extends ObjectService {

    @Override
    public Repository getRepository() throws StorageException {
        return FakeRepository.instance();
    }

    @Override
    public ProxyFactory getProxyFactory() {
        return FakeProxyFactory.getFakeFactory();
    }
}

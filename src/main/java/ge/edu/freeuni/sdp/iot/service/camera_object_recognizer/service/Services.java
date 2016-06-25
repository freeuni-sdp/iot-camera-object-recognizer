package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import com.microsoft.azure.storage.StorageException;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data.Repository;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data.RepositoryFactory;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy.ProxyFactory;

/**
 * Created by misho on 6/25/16.
 */
public class Services {
    public Repository getRepository() throws StorageException {
        return RepositoryFactory.create();
    }

    public ProxyFactory getProxyFactory() {
        return ProxyFactory.getProxyFactory();
    }

    protected boolean houseExists(String id) {
        return getProxyFactory()
                .getHouseRegistryService()
                .get(id);
    }
}

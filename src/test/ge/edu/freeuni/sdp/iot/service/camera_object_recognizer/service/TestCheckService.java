package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy.FakeProxyFactory;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy.HouseRegistryServiceProxy;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by misho on 7/11/16.
 */
public class TestCheckService extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(FakeCheckService.class);
    }

    @Test
    public void testHouseNotFound(){
        FakeProxyFactory factory = FakeProxyFactory.getFakeFactory();
        HouseRegistryServiceProxy house = mock(HouseRegistryServiceProxy.class);
        factory.setHouseRegistryService(house);
        when(house.get("1")).thenReturn(false);
        Response r = target("houses/11/check")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), r.getStatus());
    }

}

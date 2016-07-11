package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import com.microsoft.azure.storage.StorageException;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data.FakeRepository;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.model.ObjectEntity;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy.CameraProxy;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy.FakeProxyFactory;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy.GoogleApiServiceProxy;
import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.proxy.HouseRegistryServiceProxy;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestCheckService extends JerseyTest {
    private HouseRegistryServiceProxy house;
    private CameraProxy camera;
    private GoogleApiServiceProxy googleapi;

    @Before
    public void init(){
        FakeProxyFactory factory = FakeProxyFactory.getFakeFactory();
        house = mock(HouseRegistryServiceProxy.class);
        camera = mock(CameraProxy.class);
        googleapi = mock(GoogleApiServiceProxy.class);
        factory.setHouseRegistryService(house);
        factory.setCamera(camera);
        factory.setGoogleApiService(googleapi);
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(FakeCheckService.class);
    }

    @Test
    public void testHouseNotFound(){
        FakeRepository.instance().clear();
        when(house.getCameraUrl("1")).thenReturn("0.0.0.0");
        when(house.get("1")).thenReturn(false);
        Response r = target("houses/1/check")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), r.getStatus());
    }

    @Test
    public void testCameraNullUrl() {
        FakeRepository.instance().clear();
        when(house.get("1")).thenReturn(true);
        when(house.getCameraUrl("1")).thenReturn(null);
        Response r = target("houses/1/check")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), r.getStatus());
    }

    @Test
    public void checkEmptyGoogleResponse() throws IOException, GeneralSecurityException {
        FakeRepository.instance().clear();
        when(house.get("1")).thenReturn(true);
        when(house.getCameraUrl("1")).thenReturn("0.0.0.0");
        byte[] bt = new byte[1];
        when(camera.get("0.0.0.0")).thenReturn(bt);
        when(googleapi.getObjectList(bt, 50)).thenReturn(new ArrayList<String>());
        Response r = target("houses/1/check")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        JSONObject obj = new JSONObject(r.readEntity(String.class));
        assertTrue(obj.getBoolean("result"));
    }

    @Test
    public void checkForUnknownObject() throws IOException, GeneralSecurityException {
        FakeRepository.instance().clear();
        when(house.get("1")).thenReturn(true);
        when(house.getCameraUrl("1")).thenReturn("0.0.0.0");
        byte[] bt = new byte[1];
        when(camera.get("0.0.0.0")).thenReturn(bt);
        when(googleapi.getObjectList(bt, 50)).thenReturn( new ArrayList<String>() {{
            add("chair");
            add("table");
        }});
        Response r = target("houses/1/check")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        JSONObject obj = new JSONObject(r.readEntity(String.class));
        assertFalse(obj.getBoolean("result"));
        List<String> ls = new ArrayList<>();
        JSONArray arr = obj.getJSONArray("objects");
        for (int i = 0; i < arr.length(); i++) {
            ls.add(arr.getString(i));
        }
        assertEquals(2, ls.size());
        assertTrue(ls.contains("chair"));
        assertTrue(ls.contains("table"));
        assertFalse(ls.contains("rame"));
    }

    @Test
    public void checkAllKnownObjects() throws IOException, GeneralSecurityException, StorageException {
        FakeRepository.instance().clear();
        when(house.get("1")).thenReturn(true);
        when(house.getCameraUrl("1")).thenReturn("0.0.0.0");
        byte[] bt = new byte[1];
        when(camera.get("0.0.0.0")).thenReturn(bt);
        when(googleapi.getObjectList(bt, 50)).thenReturn( new ArrayList<String>() {{
            add("tebro");
            add("lamara");
        }});
        FakeRepository.instance().insertOrUpdate(ObjectEntity.fromDo("1", new ObjectDo("1", "tebro")));
        FakeRepository.instance().insertOrUpdate(ObjectEntity.fromDo("1", new ObjectDo("2", "lamara")));
        Response r = target("houses/1/check")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        JSONObject obj = new JSONObject(r.readEntity(String.class));
        System.out.println(obj.getJSONArray("objects"));
        assertTrue(obj.getBoolean("result"));
    }

    @Test
    public void checkKnownAndUknownsTogether() throws IOException, GeneralSecurityException, StorageException {
        FakeRepository.instance().clear();
        when(house.get("1")).thenReturn(true);
        when(house.getCameraUrl("1")).thenReturn("0.0.0.0");
        byte[] bt = new byte[1];
        when(camera.get("0.0.0.0")).thenReturn(bt);
        when(googleapi.getObjectList(bt, 50)).thenReturn( new ArrayList<String>() {{
            add("tebro");
            add("lamara");
            add("skameika");
        }});
        FakeRepository.instance().insertOrUpdate(ObjectEntity.fromDo("1", new ObjectDo("1", "tebro")));
        FakeRepository.instance().insertOrUpdate(ObjectEntity.fromDo("1", new ObjectDo("2", "lamara")));
        Response r = target("houses/1/check")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        JSONObject obj = new JSONObject(r.readEntity(String.class));
        assertFalse(obj.getBoolean("result"));
        List<String> ls = new ArrayList<>();
        JSONArray arr = obj.getJSONArray("objects");
        for (int i = 0; i < arr.length(); i++) {
            ls.add(arr.getString(i));
        }
        assertEquals(1, ls.size());
        assertTrue(ls.contains("skameika"));
        assertFalse(ls.contains("tebro"));
        assertFalse(ls.contains("lamara"));
    }

}

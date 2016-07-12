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
import java.lang.reflect.Array;
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
        String houseId = "1";
        String cameraUrl = "0.0.0.0";
        prepareHouseRegistry(houseId, false);
        prepareCameraUrl(houseId, cameraUrl);
        Response r = getResponse(String.format("houses/%s/check", houseId));

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), r.getStatus());
    }

    @Test
    public void testCameraNullUrl() {
        FakeRepository.instance().clear();
        String houseId = "asd";
        prepareHouseRegistry(houseId, true);
        prepareCameraUrl(houseId, null);
        Response r = getResponse(String.format("houses/%s/check", houseId));

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), r.getStatus());
    }

    @Test
    public void checkEmptyGoogleResponse() throws IOException, GeneralSecurityException {
        FakeRepository.instance().clear();
        String houseId = "#";
        String cameraUrl = "0.0.0.0";
        prepareHouseRegistry(houseId, true);
        prepareCameraUrl(houseId, cameraUrl);
        byte[] bt = new byte[1];
        prepareCamera(cameraUrl, bt);
        prepareGoogleApi(bt, 50, new ArrayList<String>());
        CheckDo obj = readEntity(String.format("houses/%s/check", houseId));

        assertTrue(obj.isResult());
    }

    @Test
    public void checkForUnknownObject() throws IOException, GeneralSecurityException {
        FakeRepository.instance().clear();
        String houseId = "-1";
        String cameraUrl = "0.0.0.0";
        prepareHouseRegistry(houseId, true);
        prepareCameraUrl(houseId, cameraUrl);
        byte[] bt = new byte[1];
        prepareCamera(cameraUrl, bt);
        prepareGoogleApi(bt, 50, new ArrayList<String>() {{
            add("chair");
            add("table");
        }});
        CheckDo obj = readEntity(String.format("houses/%s/check", houseId));
        assertFalse(obj.isResult());
        List<String> ls = obj.getObjects();

        assertEquals(2, ls.size());
        assertTrue(ls.contains("chair"));
        assertTrue(ls.contains("table"));
        assertFalse(ls.contains("rame"));
    }

    @Test
    public void checkAllKnownObjects() throws IOException, GeneralSecurityException, StorageException {
        FakeRepository.instance().clear();
        String houseId = "--";
        String cameraUrl = "0.0.0.0";
        prepareHouseRegistry(houseId, true);
        prepareCameraUrl(houseId, cameraUrl);
        byte[] bt = new byte[1];
        prepareCamera(cameraUrl, bt);
        prepareGoogleApi(bt, 50, new ArrayList<String>() {{
            add("tebro");
            add("lamara");
        }});
        updateRepository(houseId, new ArrayList<String>() {{
            add("tebro");
            add("lamara");
        }});
        CheckDo obj = readEntity(String.format("houses/%s/check", houseId));

        assertTrue(obj.isResult());
    }

    @Test
    public void checkKnownAndUknownsTogether() throws IOException, GeneralSecurityException, StorageException {
        FakeRepository.instance().clear();
        String houseId = "1";
        String cameraUrl = "0.0.0.0";
        prepareHouseRegistry(houseId, true);
        prepareCameraUrl(houseId, cameraUrl);
        byte[] bt = new byte[1];
        prepareCamera(cameraUrl, bt);
        prepareGoogleApi(bt, 50, new ArrayList<String>() {{
            add("tebro");
            add("lamara");
            add("skameika");
        }});
        updateRepository(houseId, new ArrayList<String>() {{
            add("tebro");
            add("lamara");
        }});

        CheckDo obj = readEntity(String.format("houses/%s/check", houseId));
        List<String> ls = obj.getObjects();

        assertFalse(obj.isResult());

        assertEquals(1, ls.size());

        assertTrue(ls.contains("skameika"));
        assertFalse(ls.contains("tebro"));
        assertFalse(ls.contains("lamara"));
    }


    private void prepareHouseRegistry(String houseId, boolean response) {
        when(house.get(houseId)).thenReturn(response);
    }

    private void prepareCameraUrl(String houseId, String response) {
        when(house.getCameraUrl(houseId)).thenReturn(response);
    }

    private void prepareCamera(String url, byte[] response) {
        when(camera.get(url)).thenReturn(response);
    }

    private void prepareGoogleApi(byte[] request, int max, ArrayList<String> response) throws IOException, GeneralSecurityException {
        when(googleapi.getObjectList(request, max)).thenReturn(response);
    }

    private void updateRepository(String houseId, ArrayList<String> arr) throws StorageException {
        int i = 0;
        for (String elem : arr) {
            FakeRepository.instance().insertOrUpdate(ObjectEntity.fromDo(houseId,
                    new ObjectDo(String.valueOf(i++), elem)));
        }
    }

    private Response getResponse(String url) {
        return target(url)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }

    private CheckDo readEntity(String url) {
        return getResponse(url).readEntity(CheckDo.class);
    }
}

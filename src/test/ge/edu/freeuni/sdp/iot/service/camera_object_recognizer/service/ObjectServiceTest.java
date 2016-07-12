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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectServiceTest extends JerseyTest {

    private static final String[] OBJECTS = {"person", "dog", "cat", "table"};

    @Mock
    private CameraProxy cameraProxy;
    @Mock
    private GoogleApiServiceProxy googleApiServiceProxy;
    @Mock
    private HouseRegistryServiceProxy houseRegistryServiceProxy;

    @Override
    protected Application configure() {
        return new ResourceConfig(FakeObjectService.class);
    }

    @Before
    public void setUpChild() throws Exception {
        MockitoAnnotations.initMocks(this);
        FakeProxyFactory.getFakeFactory().setCamera(cameraProxy);
        FakeProxyFactory.getFakeFactory().setGoogleApiService(googleApiServiceProxy);
        FakeProxyFactory.getFakeFactory().setHouseRegistryService(houseRegistryServiceProxy);
    }

    @Test
    public void testGetList_except200emptyList() {
        FakeRepository.instance().clear();
        when(houseRegistryServiceProxy.get("1")).thenReturn(true);
        Response actual = getTarget("1")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        ObjectDo[] actualDo = actual.readEntity(ObjectDo[].class);
        assertThat(actual.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertNotNull(actualDo);
        assertThat(actualDo.length, is(0));
    }

    @Test
    public void testGetList_except200nonemptyList() throws StorageException {
        FakeRepository.instance().clear();
        String houseId = "1";
        ObjectEntity expected = getRandomObjectEntity(houseId);
        FakeRepository.instance().insertOrUpdate(expected);
        when(houseRegistryServiceProxy.get(houseId)).thenReturn(true);
        Response actual = getTarget("1")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        ObjectDo[] actualDo = actual.readEntity(ObjectDo[].class);
        assertThat(actual.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(actualDo.length, is(1));
        assertThat(actualDo[0], is(equalTo(expected.toDo())));
    }

    @Test
    public void testGetList_except404wrongHouseId() {
        FakeRepository.instance().clear();
        String houseId = "1";
        when(houseRegistryServiceProxy.get(houseId)).thenReturn(false);
        Response actual = getTarget(houseId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertThat(actual.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void testGetObjectById_except200ObjectDo() throws StorageException {
        FakeRepository.instance().clear();
        String houseId = "1";
        ObjectEntity expected = getRandomObjectEntity(houseId);
        FakeRepository.instance().insertOrUpdate(expected);
        when(houseRegistryServiceProxy.get(houseId)).thenReturn(true);
        Response actual = getTarget(houseId, expected.getRowKey())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        ObjectDo actualDo = actual.readEntity(ObjectDo.class);
        assertThat(actual.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(actualDo, is(equalTo(expected.toDo())));
    }

    @Test
    public void testGetObjectById_except404WrongHouseId() throws StorageException {
        FakeRepository.instance().clear();
        String houseId = "1";
        ObjectEntity expected = getRandomObjectEntity(houseId);
        FakeRepository.instance().insertOrUpdate(expected);
        when(houseRegistryServiceProxy.get(houseId)).thenReturn(false);
        Response actual = getTarget(houseId, expected.getRowKey())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertThat(actual.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void testGetObjectById_except404WrongObjectId() throws StorageException {
        FakeRepository.instance().clear();
        String houseId = "1";
        ObjectEntity expected = getRandomObjectEntity(houseId);
        FakeRepository.instance().insertOrUpdate(expected);
        when(houseRegistryServiceProxy.get(houseId)).thenReturn(true);
        Response actual = getTarget(houseId, "zoro")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertThat(actual.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void testAddObject_except200() {
        FakeRepository.instance().clear();
        String houseId = "1";
        ObjectDo expected = getRandomObjectDo();
        when(houseRegistryServiceProxy.get(houseId)).thenReturn(true);
        Response actual = getTarget(houseId)
                .request()
                .post(Entity.entity(expected, MediaType.APPLICATION_JSON_TYPE));
        ObjectDo actualDo = actual.readEntity(ObjectDo.class);
        assertThat(actual.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(actualDo.getType(), is(equalTo(expected.getType())));
        assertThat(FakeRepository.instance().contains(houseId, actualDo.getId()), is(true));
    }

    @Test
    public void testAddObject_except404WrongHouseId() {
        FakeRepository.instance().clear();
        String houseId = "1";
        ObjectDo expected = getRandomObjectDo();
        when(houseRegistryServiceProxy.get(houseId)).thenReturn(false);
        Response actual = getTarget(houseId)
                .request()
                .post(Entity.entity(expected, MediaType.APPLICATION_JSON_TYPE));
        assertThat(actual.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void testUpdateObjectById_except200UpdatedObjectDo() throws StorageException {
        FakeRepository.instance().clear();
        String houseId = "1";
        ObjectEntity randomObjectEntity = getRandomObjectEntity(houseId);
        FakeRepository.instance().insertOrUpdate(randomObjectEntity);
        ObjectDo expected = randomObjectEntity.toDo();
        expected.setType(getRandomType());
        when(houseRegistryServiceProxy.get(houseId)).thenReturn(true);
        Response actual = getTarget(houseId, expected.getId())
                .request()
                .put(Entity.entity(expected, MediaType.APPLICATION_JSON_TYPE));
        ObjectDo actualDo = actual.readEntity(ObjectDo.class);
        assertThat(actual.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(actualDo, is(equalTo(expected)));
        assertThat(FakeRepository.instance().contains(houseId, actualDo.getId()), is(true));
    }

    @Test
    public void testUpdateObjectById_except404WrongHouseId() throws StorageException {
        FakeRepository.instance().clear();
        String houseId = "1";
        ObjectEntity randomObjectEntity = getRandomObjectEntity(houseId);
        FakeRepository.instance().insertOrUpdate(randomObjectEntity);
        ObjectDo expected = randomObjectEntity.toDo();
        expected.setType(getRandomType());
        when(houseRegistryServiceProxy.get(houseId)).thenReturn(false);
        Response actual = getTarget(houseId, expected.getId())
                .request()
                .put(Entity.entity(expected, MediaType.APPLICATION_JSON_TYPE));
        assertThat(actual.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void testUpdateObjectById_except404WrongObjectId() throws StorageException {
        FakeRepository.instance().clear();
        String houseId = "1";
        ObjectEntity randomObjectEntity = getRandomObjectEntity(houseId);
        FakeRepository.instance().insertOrUpdate(randomObjectEntity);
        ObjectDo expected = randomObjectEntity.toDo();
        expected.setType(getRandomType());
        when(houseRegistryServiceProxy.get(houseId)).thenReturn(true);
        Response actual = getTarget(houseId, "zoro")
                .request()
                .put(Entity.entity(expected, MediaType.APPLICATION_JSON_TYPE));
        assertThat(actual.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void testDeleteObjectById_except200() throws StorageException {
        FakeRepository.instance().clear();
        String houseId = "1";
        ObjectEntity expected = getRandomObjectEntity(houseId);
        FakeRepository.instance().insertOrUpdate(expected);
        when(houseRegistryServiceProxy.get(houseId)).thenReturn(true);
        Response actual = getTarget(houseId, expected.getRowKey())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        assertThat(actual.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(FakeRepository.instance().contains(houseId, expected.getRowKey()), is(false));
    }

    @Test
    public void testDeleteObjectById_except404WrongHouseId() throws StorageException {
        FakeRepository.instance().clear();
        String houseId = "1";
        ObjectEntity randomObjectEntity = getRandomObjectEntity(houseId);
        FakeRepository.instance().insertOrUpdate(randomObjectEntity);
        ObjectDo expected = randomObjectEntity.toDo();
        expected.setType(getRandomType());
        when(houseRegistryServiceProxy.get(houseId)).thenReturn(false);
        Response actual = getTarget(houseId, expected.getId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        assertThat(actual.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void testDeleteObjectById_except404WrongObjectId() throws StorageException {
        FakeRepository.instance().clear();
        String houseId = "1";
        ObjectEntity randomObjectEntity = getRandomObjectEntity(houseId);
        FakeRepository.instance().insertOrUpdate(randomObjectEntity);
        ObjectDo expected = randomObjectEntity.toDo();
        expected.setType(getRandomType());
        when(houseRegistryServiceProxy.get(houseId)).thenReturn(true);
        Response actual = getTarget(houseId, "zoro")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        assertThat(actual.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    private WebTarget getTarget(String houseId) {
        String path = String.format("/houses/%s/objects", houseId);
        return target(path);
    }

    private WebTarget getTarget(String houseId, String objectId) {
        String path = String.format("/houses/%s/objects/%s", houseId, objectId);
        return target(path);
    }

    private ObjectEntity getRandomObjectEntity(String houseId) {
        return ObjectEntity.fromDo(houseId, getRandomObjectDo());
    }

    private ObjectDo getRandomObjectDo() {
        return new ObjectDo(UUID.randomUUID().toString(), getRandomType());
    }

    private String getRandomType() {
        int index = new Random().nextInt(OBJECTS.length);
        return OBJECTS[index];
    }

}
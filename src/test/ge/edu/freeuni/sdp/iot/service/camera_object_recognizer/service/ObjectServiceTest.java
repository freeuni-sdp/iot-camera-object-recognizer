package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.data.FakeRepository;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import static org.junit.Assert.*;

public class ObjectServiceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(FakeObjectService.class);
    }

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testGetList_except200emptyList() {

    }

    @Test
    public void testGetList_except200nonemptyList() {

    }

    @Test
    public void testGetList_except404wrongHouseId() {

    }

    @Test
    public void testGetObjectById_except200ObjectDo() {

    }

    @Test
    public void testGetObjectById_except404WrongHouseId() {

    }

    @Test
    public void testGetObjectById_except404WrongObjectId() {

    }

    @Test
    public void testAddObject_except200() {

    }

    @Test
    public void testAddObject_except404WrongHouseId() {

    }

    @Test
    public void testUpdateObjectById_except200UpdatedObjectDo() {

    }

    @Test
    public void testUpdateObjectById_except404WrongHouseId() {

    }

    @Test
    public void testUpdateObjectById_except404WrongObjectId() {

    }

    @Test
    public void testDeleteObjectById_except200() {

    }

    @Test
    public void testDeleteObjectById_except404WrongHouseId() {

    }

    @Test
    public void testDeleteObjectById_except404WrongObjectId() {

    }

    private WebTarget getTarget(String houseId) {
        String path = String.format("/houses/%s", houseId);
        return target(path);
    }

    private WebTarget getTarget(String houseId, String objectId) {
        String path = String.format("/houses/%s/objects/%s", houseId, objectId);
        return target(path);
    }

}
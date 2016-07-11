package ge.edu.freeuni.sdp.iot.service.camera_object_recognizer.service;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;

import javax.ws.rs.core.Application;

import static org.junit.Assert.*;

public class ObjectServiceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(PingService.class);
    }

    @Before
    public void setUp() throws Exception {

    }

}
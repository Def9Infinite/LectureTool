package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;
import nl.tudelft.oopp.project.entities.Session;
import org.junit.jupiter.api.Test;


public class PollingEventsControllerTest {
    @Test
    public static void testAddListener() {
        Session s = new Session(UUID.randomUUID(), UUID.randomUUID(), "test");
        PollingEventsController.addListener(s, null);
        assertNotNull(PollingEventsController.getListeners());
    }

    @Test
    public static void testEmit() {
        Session s = new Session(UUID.randomUUID(), UUID.randomUUID(), "test");
        PollingEventsController.addListener(s, null);
        PollingEventsController.emit(s, "test");
        assertEquals(0, PollingEventsController.getListeners().get(s.getId()).size());
    }

}

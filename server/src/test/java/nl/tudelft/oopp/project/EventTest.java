package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import nl.tudelft.oopp.project.entities.Event;
import nl.tudelft.oopp.project.entities.Poll;
import nl.tudelft.oopp.project.entities.PollOption;
import nl.tudelft.oopp.project.entities.PollResponse;
import nl.tudelft.oopp.project.entities.Question;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.SpeedFeedback;
import nl.tudelft.oopp.project.entities.Upvote;
import nl.tudelft.oopp.project.entities.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;





public class EventTest {

    private List<Event> events;

    /**
     * Creates events before each test.
     */


    @Test
    public void constructorTest1() {
        Event event = new Event();

        assertNotNull(event);
    }

    @Test
    public void setGetIdTest() {
        Event event = new Event();
        event.setId(1);
        assertEquals(1, event.getId());
    }

    @Test
    public void setGetSessionTest() {
        Session session = new Session();
        session.setSessionName("sessionName");

        Event event = new Event();
        event.setSession(session);

        assertEquals(session, event.getSession());
    }

    @Test
    public void getSetTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Event event = new Event();
        event.setTimestamp(timestamp);
        assertEquals(timestamp,event.getTimestamp());
    }

    @Test
    public void getSetEvent() {
        String string = "Test";
        Event event = new Event();
        event.setEvent(string);
        assertEquals(string,event.getEvent());
    }

    @Test
    public void equals() {
        Event event = new Event();
        Event event1 = event;
        assertTrue(event.equals(event1));
    }

    @Test
    public void equalsNull() {
        Event event = new Event();
        Event event1 = null;
        assertFalse(event.equals(event1));
    }

    @Test
    public void equalsFalse() {
        Session session = new Session();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Event event = new Event(session,timestamp,"Test");
        Event event1 = new Event(session,timestamp,"Hey");
        assertFalse(event.equals(event1));
    }

    @Test
    public void hashCodeTest() {
        Session session = new Session();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Event event = new Event(session,timestamp,"Test");
        Event event1 = new Event(session,timestamp,"Hey");
        assertFalse(event.hashCode() == event1.hashCode());
    }



}

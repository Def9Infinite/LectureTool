package nl.tudelft.oopp.project;

import java.util.UUID;

import nl.tudelft.oopp.project.exceptions.NoExistingSessionInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class SessionTest {
    @Test
    public void setSessionNameTest() {
        Session session = new Session(1, UUID.fromString("c0709b40-19c9-4928-8afa-0ce895d4d482"),
                UUID.fromString("c0709b40-19c9-4928-8afa-0ce895d4d482"),"");
        session.setSessionName("test");
        Assertions.assertEquals("test",session.getSessionName());
    }

    @Test
    public void toStringTest() {
        Session session = new Session(1, UUID.fromString("c0709b40-19c9-4928-8afa-0ce895d4d482"),
                UUID.fromString("c0709b40-19c9-4928-8afa-0ce895d4d482"),"");
        Assertions.assertEquals("Session{ id=1, powerToken=c0709b40-19c9-4928-8afa-0ce895d4d482,"
                + " studentToken=c0709b40-19c9-4928-8afa-0ce895d4d482, sessionName= }",session.toString());
    }

    @Test
    public void equalsTest() {
        Session session = new Session(1, UUID.fromString("c0709b40-19c9-4928-8afa-0ce895d4d482"),
                UUID.fromString("c0709b40-19c9-4928-8afa-0ce895d4d482"),"");
        Session session1 = session;
        Assertions.assertTrue(session.equals(session));
    }

    @Test
    public void hashTest() {
        Session session = new Session(1, UUID.fromString("c0709b40-19c9-4928-8afa-0ce895d4d482"),
                UUID.fromString("c0709b40-19c9-4928-8afa-0ce895d4d482"),"");
        Session session1 = new Session(1, UUID.fromString("c0709b40-19c9-4928-8afa-0ce895d4d482"),
                UUID.fromString("c0709b40-19c9-4928-8afa-0ce895d4d482"),"");
        Assertions.assertTrue(session.hashCode() == session1.hashCode());
    }

    @Test
    public void setGetSession() throws NoExistingSessionInstance {
        Session session = new Session(1, UUID.fromString("c0709b40-19c9-4928-8afa-0ce895d4d482"),
                UUID.fromString("c0709b40-19c9-4928-8afa-0ce895d4d482"),"");
        Session.setInstance(session);
        Assertions.assertEquals(session,Session.getInstance());
    }

    @Test
    public void equalsNull() {
        Session session = new Session("Test","1-2-3");
        Session session2 = null;
        Assertions.assertFalse(session.equals(session2));
    }
}

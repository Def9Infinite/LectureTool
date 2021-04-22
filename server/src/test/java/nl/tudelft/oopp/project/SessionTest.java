package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.project.entities.Session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class SessionTest {

    private List<Session> sessions;

    /**
     * Creates sessions before each test.
     */
    @BeforeEach
    public void createTestSessions() {
        sessions = new ArrayList<>();

        // session1
        long id1 = 1;
        UUID powerToken1 = UUID.randomUUID();
        UUID studentToken1 = UUID.randomUUID();
        String sessionName1 = "session1";
        Session session1 = new Session(powerToken1, studentToken1, sessionName1);
        session1.setId(id1);
        sessions.add(session1);

        // session2 - equals to session1
        Session session2 = new Session(powerToken1, studentToken1, sessionName1);
        session2.setId(id1);
        sessions.add(session2);

        // session3 - different from session1
        long id3 = 3;
        UUID powerToken3 = UUID.randomUUID();
        UUID studentToken3 = UUID.randomUUID();
        String sessionName3 = "session3";
        Session session3 = new Session(powerToken3, studentToken3, sessionName3);
        session3.setId(id3);
        sessions.add(session3);

        // session4 - equals to session1
        Session session4 = new Session(powerToken1, studentToken1, sessionName1);
        session4.setId(id1);
        sessions.add(session4);

        // session5 - equals to session1
        Session session5 = new Session(powerToken1, studentToken1, sessionName1);
        session5.setId(id1);
        sessions.add(session5);
    }

    @Test
    public void constructorTest1() {
        Session session = new Session();
        assertNotNull(session);
    }

    @Test
    public void constructorTest2() {
        Session session = new Session(UUID.randomUUID(), UUID.randomUUID(), "session");
        session.setId(1);
        assertNotNull(session);
    }

    @Test
    public void setGetIdTest() {
        Session session = new Session();
        session.setId(1);
        assertEquals(1, session.getId());
    }

    @Test
    public void setGetPowerTokenTest() {
        Session session = new Session();
        UUID powerToken = UUID.randomUUID();
        session.setPowerToken(powerToken);
        assertEquals(powerToken, session.getPowerToken());
    }

    @Test
    public void setGetStudentTokenTest() {
        Session session = new Session();
        UUID studentToken = UUID.randomUUID();
        session.setStudentToken(studentToken);
        assertEquals(studentToken, session.getStudentToken());
    }

    @Test
    public void setGetSessionNameTest() {
        Session session = new Session();
        String sessionName = "session";
        session.setSessionName(sessionName);
        assertEquals(sessionName, session.getSessionName());
    }

    @Test
    public void equalsReflexiveTest() {
        Session session1 = sessions.get(0);
        assertEquals(session1, session1);
    }

    @Test
    public void equalsSameTest() {
        Session session1 = sessions.get(0);
        Session session2 = sessions.get(1);
        assertEquals(session1, session2);
    }

    @Test
    public void equalsSymmetricTest() {
        Session session1 = sessions.get(0);
        Session session2 = sessions.get(1);
        boolean equals1 = session1.equals(session2);
        boolean equals2 = session2.equals(session1);
        assertEquals(true, equals1 == equals2);
    }

    @Test
    public void equalsDifferentIdTest() {
        Session session1 = sessions.get(0);
        Session session2 = sessions.get(1);
        long newId = 2;
        session2.setId(newId);
        assertNotEquals(session1, session2);
    }

    @Test
    public void equalsDifferentPowerTokenTest() {
        Session session1 = sessions.get(0);
        Session session2 = sessions.get(1);
        UUID powerToken = session2.getPowerToken();
        UUID newPowerToken = UUID.randomUUID();
        while (newPowerToken.equals(powerToken)) {
            newPowerToken = UUID.randomUUID();
        }
        session2.setPowerToken(newPowerToken);
        assertNotEquals(session1, session2);
    }

    @Test
    public void equalsDifferentStudentTokenTest() {
        Session session1 = sessions.get(0);
        Session session2 = sessions.get(1);
        UUID studentToken = session2.getStudentToken();
        UUID newStudentToken = UUID.randomUUID();
        while (newStudentToken.equals(studentToken)) {
            newStudentToken = UUID.randomUUID();
        }
        session2.setStudentToken(newStudentToken);
        assertNotEquals(session1, session2);
    }

    @Test
    public void equalsDifferentSessionNameTest() {
        Session session1 = sessions.get(0);
        Session session2 = sessions.get(1);
        String newSessionName = "session2";
        session2.setSessionName(newSessionName);
        assertNotEquals(session1, session2);
    }

    @Test
    public void equalsTransitiveTest() {
        Session session1 = sessions.get(0);
        Session session2 = sessions.get(1);
        Session session4 = sessions.get(3);
        boolean s1EqualsS2 = session1.equals(session2);
        boolean s2EqualsS4 = session2.equals(session4);
        boolean s1EqualsS4Actual = session1.equals(session4);
        boolean s1EqualsS4Theoretical;
        if (s1EqualsS2 == s2EqualsS4) {
            s1EqualsS4Theoretical = true;
        } else {
            s1EqualsS4Theoretical = false;
        }
        assertEquals(s1EqualsS4Actual, s1EqualsS4Theoretical);
    }

    @Test
    public void equalsConsistentTest() {
        Session session1 = sessions.get(0);
        Session session5 = sessions.get(4);
        boolean equals1 = session1.equals(session5);
        session5.setId(2);
        boolean equals2 = session1.equals(session5);

        assertNotEquals(equals1, equals2);
    }

    @Test
    public void equalsNullTest() {
        Session session1 = sessions.get(0);
        Session session5 = null;

        assertEquals(false, session1.equals(session5));
    }

    @Test
    public void hashCodeEqualsConsistentTest() {
        Session session1 = sessions.get(0);
        Session session5 = sessions.get(4);
        boolean equalsHashCode = session1.hashCode() == session5.hashCode();

        assertEquals(true, equalsHashCode);
    }

    @Test
    public void banIpCreateSetTest() {
        String ipToBan = "130.161.128.82";
        Session session1 = sessions.get(0);

        assertNull(session1.getBannedIps());

        session1.banIp(ipToBan);
        assertNotNull(session1.getBannedIps());
    }

    @Test
    public void banIpAddSetTest() {
        String ipToBan = "130.161.128.82";
        String ipToBan2 = "130.161.128.83";
        Session session1 = sessions.get(0);

        assertNull(session1.getBannedIps());

        session1.banIp(ipToBan);
        session1.banIp(ipToBan2);
        assertTrue(session1.getBannedIps().contains(ipToBan));
        assertTrue(session1.getBannedIps().contains(ipToBan2));
    }

    @Test
    public void isIpBannedNoNullPointerExceptionTest() {
        String ipToCheck = "130.161.128.82";
        Session session1 = sessions.get(0);

        assertNull(session1.getBannedIps());
        assertFalse(session1.ipIsBanned(ipToCheck));
    }

    @Test
    public void ipIsBannedCheck() {
        final String ipToBan = "130.161.128.82";
        final String ipNotBanned = "130.161.128.83";
        Session session1 = sessions.get(0);

        assertNull(session1.getBannedIps());
        session1.banIp(ipToBan);

        assertTrue(session1.ipIsBanned(ipToBan));
        assertFalse(session1.ipIsBanned(ipNotBanned));
    }
}

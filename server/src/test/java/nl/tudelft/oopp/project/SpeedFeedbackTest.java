package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.SpeedFeedback;
import nl.tudelft.oopp.project.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class SpeedFeedbackTest {

    private List<SpeedFeedback> speedFeedbacks;

    /**
     * Creates speedFeedbacks before each test.
     */
    @BeforeEach
    public void createTestSpeedFeedbacks() {
        speedFeedbacks = new ArrayList<>();

        // speedFeedback1
        long id1 = 1;

        UUID token1 = UUID.randomUUID();
        int role1 = 1;
        String nickname1 = "user1";
        String ipAddress1 = "ip1";
        UUID powerToken1 = UUID.randomUUID();
        UUID studentToken1 = UUID.randomUUID();
        String sessionName1 = "session1";
        Session session1 = new Session(powerToken1, studentToken1, sessionName1);
        session1.setId(1);
        User user1 = new User(token1, role1, nickname1, ipAddress1, session1);
        user1.setId(id1);

        int speed1 = 1;

        SpeedFeedback speedFeedback1 = new SpeedFeedback(session1, user1, speed1);
        speedFeedback1.setId(1);

        speedFeedbacks.add(speedFeedback1);

        // speedFeedback2 - equals to speedFeedback1
        SpeedFeedback speedFeedback2 = new SpeedFeedback(session1, user1, speed1);
        speedFeedback2.setId(1);

        speedFeedbacks.add(speedFeedback2);

        // speedFeedback3 - different from speedFeedback1
        long id3 = 1;

        UUID token3 = UUID.randomUUID();
        int role3 = 1;
        String nickname3 = "user1";
        String ipAddress3 = "ip1";
        UUID powerToken3 = UUID.randomUUID();
        UUID studentToken3 = UUID.randomUUID();
        String sessionName3 = "session3";
        Session session3 = new Session(powerToken3, studentToken3, sessionName3);
        session3.setId(3);
        User user3 = new User(token3, role3, nickname3, ipAddress3, session3);
        user3.setId(id3);

        int speed3 = 3;

        SpeedFeedback speedFeedback3 = new SpeedFeedback(session3, user3, speed3);
        speedFeedback3.setId(3);

        speedFeedbacks.add(speedFeedback3);

        // speedFeedback4 - equals to speedFeedback1
        SpeedFeedback speedFeedback4 = new SpeedFeedback(session1, user1, speed1);
        speedFeedback4.setId(1);

        speedFeedbacks.add(speedFeedback2);

        // speedFeedback5 - equals to uspeedFeedback1
        SpeedFeedback speedFeedback5 = new SpeedFeedback(session1, user1, speed1);
        speedFeedback5.setId(1);

        speedFeedbacks.add(speedFeedback5);
    }

    @Test
    public void constructorTest1() {
        SpeedFeedback speedFeedback = new SpeedFeedback();
        assertNotNull(speedFeedback);
    }

    @Test
    public void constructorTest2() {
        Session session = new Session();
        User user = new User(UUID.randomUUID(), 1, "user", "ip", session);
        int speed = 1;
        SpeedFeedback speedFeedback = new SpeedFeedback(session, user, speed);
        assertNotNull(speedFeedback);
    }

    @Test
    public void setGetIdTest() {
        SpeedFeedback speedFeedback = new SpeedFeedback();
        speedFeedback.setId(1);
        assertEquals(1, speedFeedback.getId());
    }

    @Test
    public void setGetSessionTest() {
        SpeedFeedback speedFeedback = new SpeedFeedback();
        Session session = new Session();
        String sessionName = "sessionName";
        session.setSessionName(sessionName);
        speedFeedback.setSession(session);
        assertEquals(session, speedFeedback.getSession());
    }

    @Test
    public void setGetUserTest() {
        SpeedFeedback speedFeedback = new SpeedFeedback();
        User user = new User();
        String nickname = "user";
        user.setNickname(nickname);
        speedFeedback.setUser(user);
        assertEquals(user, speedFeedback.getUser());
    }

    @Test
    public void equalsReflexiveTest() {
        SpeedFeedback speedFeedback1 = speedFeedbacks.get(0);
        assertEquals(speedFeedback1, speedFeedback1);
    }

    @Test
    public void equalsSameTest() {
        SpeedFeedback speedFeedback1 = speedFeedbacks.get(0);
        SpeedFeedback speedFeedback2 = speedFeedbacks.get(1);
        assertEquals(speedFeedback1, speedFeedback2);
    }

    @Test
    public void equalsSymmetricTest() {
        SpeedFeedback speedFeedback1 = speedFeedbacks.get(0);
        SpeedFeedback speedFeedback2 = speedFeedbacks.get(1);
        boolean equals1 = speedFeedback1.equals(speedFeedback2);
        boolean equals2 = speedFeedback2.equals(speedFeedback1);
        assertEquals(true, equals1 == equals2);
    }

    @Test
    public void equalsDifferentIdTest() {
        SpeedFeedback speedFeedback1 = speedFeedbacks.get(0);
        SpeedFeedback speedFeedback2 = speedFeedbacks.get(1);
        long newId = 2;
        speedFeedback2.setId(newId);
        assertNotEquals(speedFeedback1, speedFeedback2);
    }

    @Test
    public void equalsDifferentSessionTest() {
        SpeedFeedback speedFeedback1 = speedFeedbacks.get(0);
        SpeedFeedback speedFeedback2 = speedFeedbacks.get(1);
        Session newSession = new Session();
        newSession.setId(2);
        speedFeedback2.setSession(newSession);

        assertNotEquals(speedFeedback1, speedFeedback2);
    }

    @Test
    public void equalsDifferentUserTest() {
        SpeedFeedback speedFeedback1 = speedFeedbacks.get(0);
        SpeedFeedback speedFeedback2 = speedFeedbacks.get(1);
        User newUser = new User();
        newUser.setId(2);
        speedFeedback2.setUser(newUser);

        assertNotEquals(speedFeedback1, speedFeedback2);
    }

    @Test
    public void equalsTransitiveTest() {
        SpeedFeedback speedFeedback1 = speedFeedbacks.get(0);
        SpeedFeedback speedFeedback2 = speedFeedbacks.get(1);
        SpeedFeedback speedFeedback4 = speedFeedbacks.get(3);
        boolean s1EqualsS2 = speedFeedback1.equals(speedFeedback2);
        boolean s2EqualsS4 = speedFeedback2.equals(speedFeedback4);
        boolean s1EqualsS4Actual = speedFeedback1.equals(speedFeedback4);
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
        SpeedFeedback speedFeedback1 = speedFeedbacks.get(0);
        SpeedFeedback speedFeedback5 = speedFeedbacks.get(4);
        boolean equals1 = speedFeedback1.equals(speedFeedback5);
        speedFeedback5.setId(2);
        boolean equals2 = speedFeedback1.equals(speedFeedback5);

        assertNotEquals(equals1, equals2);
    }

    @Test
    public void hashCodeEqualsConsistentTest() {
        SpeedFeedback speedFeedback1 = speedFeedbacks.get(0);
        SpeedFeedback speedFeedback5 = speedFeedbacks.get(4);
        boolean equalsHashCode = speedFeedback1.hashCode() == speedFeedback5.hashCode();

        assertEquals(true, equalsHashCode);
    }

    //    /**
    //     * This test takes longer than usual, depending on how fast two new
    //     * different objects get the same hash code.
    //     * The two objects are different, because their sessions are different.
    //     * In theory there should be 2 different objects with the same hashCode.
    //     * In practice, the test does not find them.
    //     */
    //    @Test
    //    public void hashCodeCollisionsTest() {
    //        SpeedFeedback speedFeedback1 = new SpeedFeedback();
    //        speedFeedback1.setId(1);
    //        SpeedFeedback speedFeedback2 = new SpeedFeedback();
    //        speedFeedback2.setId(2);
    //        boolean sameHashCode = false;
    //        boolean differentHashCodeInitially = false;
    //
    //        while (speedFeedback1.hashCode() != speedFeedback2.hashCode()) {
    //            differentHashCodeInitially = true;
    //
    //            speedFeedback1 = new SpeedFeedback();
    //            speedFeedback1.setId(1);
    //            speedFeedback2 = new SpeedFeedback();
    //            speedFeedback2.setId(2);
    //
    //            if (speedFeedback1.hashCode() == speedFeedback2.hashCode()) {
    //                sameHashCode = true;
    //            }
    //        }
    //
    //        if (!differentHashCodeInitially) {
    //            sameHashCode = true;
    //        }
    //
    //        assertEquals(true, sameHashCode);
    //    }
}

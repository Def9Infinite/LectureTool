package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import nl.tudelft.oopp.project.entities.Poll;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class PollTest {

    private List<Poll> polls;

    /**
     * Creates polls before each test.
     */
    @BeforeEach
    public void createTestPolls() {
        polls = new ArrayList<>();

        // poll1
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

        String pollText1 = "pollText";

        Poll poll1 = new Poll(session1, pollText1, user1);
        poll1.setId(id1);

        polls.add(poll1);

        // poll2 - equals to poll1
        Poll poll2 = new Poll(session1, pollText1, user1);
        poll2.setId(id1);

        polls.add(poll2);

        // poll3 - different from poll1
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

        String pollText3 = "pollText3";

        Poll poll3 = new Poll(session3, pollText3, user3);
        poll3.setId(id3);

        polls.add(poll3);

        // poll4 - equals to poll1
        Poll poll4 = new Poll(session1, pollText1, user1);
        poll4.setId(id1);

        polls.add(poll4);

        // poll5 - equals to poll1
        Poll poll5 = new Poll(session1, pollText1, user1);
        poll5.setId(id1);

        polls.add(poll5);
    }

    @Test
    public void constructorTest1() {
        Poll poll = new Poll();
        assertNotNull(poll);
    }

    @Test
    public void constructorTest2() {
        Session session = new Session();
        User user = new User(UUID.randomUUID(), 1, "user", "ip", session);
        String pollText = "pollText";
        Poll poll = new Poll(session, pollText, user);
        assertNotNull(poll);
    }

    @Test
    public void setGetIdTest() {
        Poll poll = new Poll();
        poll.setId(1);
        assertEquals(1, poll.getId());
    }

    @Test
    public void setGetSessionTest() {
        Poll poll = new Poll();
        Session session = new Session();
        String sessionName = "sessionName";
        session.setSessionName(sessionName);
        poll.setSession(session);
        assertEquals(session, poll.getSession());
    }

    @Test
    public void setGetUserTest() {
        Poll poll = new Poll();
        User user = new User();
        String nickname = "user";
        user.setNickname(nickname);
        poll.setUser(user);
        assertEquals(user, poll.getUser());
    }

    @Test
    public void equalsReflexiveTest() {
        Poll poll1 = polls.get(0);
        assertEquals(poll1, poll1);
    }

    @Test
    public void equalsSameTest() {
        Poll poll1 = polls.get(0);
        Poll poll2 = polls.get(1);
        assertEquals(poll1, poll2);
    }

    @Test
    public void equalsSymmetricTest() {
        Poll poll1 = polls.get(0);
        Poll poll2 = polls.get(1);
        boolean equals1 = poll1.equals(poll2);
        boolean equals2 = poll2.equals(poll1);
        assertEquals(true, equals1 == equals2);
    }

    @Test
    public void equalsDifferentIdTest() {
        Poll poll1 = polls.get(0);
        Poll poll2 = polls.get(1);
        long newId = 2;
        poll2.setId(newId);
        assertNotEquals(poll1, poll2);
    }

    @Test
    public void equalsDifferentSessionTest() {
        Poll poll1 = polls.get(0);
        Poll poll2 = polls.get(1);
        Session newSession = new Session();
        newSession.setId(2);
        poll2.setSession(newSession);

        assertNotEquals(poll1, poll2);
    }

    @Test
    public void equalsDifferentUserTest() {
        Poll poll1 = polls.get(0);
        Poll poll2 = polls.get(1);
        User newUser = new User();
        newUser.setId(2);
        poll2.setUser(newUser);

        assertNotEquals(poll1, poll2);
    }

    @Test
    public void equalsTransitiveTest() {
        Poll poll1 = polls.get(0);
        Poll poll2 = polls.get(1);
        Poll poll4 = polls.get(3);
        boolean p1EqualsP2 = poll1.equals(poll2);
        boolean p2EqualsP4 = poll2.equals(poll4);
        boolean p1EqualsP4Actual = poll1.equals(poll4);
        boolean p1EqualsP4Theoretical;
        if (p1EqualsP2 == p2EqualsP4) {
            p1EqualsP4Theoretical = true;
        } else {
            p1EqualsP4Theoretical = false;
        }
        assertEquals(p1EqualsP4Actual, p1EqualsP4Theoretical);
    }

    @Test
    public void equalsConsistentTest() {
        Poll poll1 = polls.get(0);
        Poll poll5 = polls.get(4);
        boolean equals1 = poll1.equals(poll5);
        poll5.setId(2);
        boolean equals2 = poll1.equals(poll5);

        assertNotEquals(equals1, equals2);
    }

    @Test
    public void hashCodeEqualsConsistentTest() {
        Poll poll1 = polls.get(0);
        Poll poll5 = polls.get(4);
        boolean equalsHashCode = poll1.hashCode() == poll5.hashCode();

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
    //        Poll poll1 = new Poll();
    //        poll1.setId(1);
    //        Poll poll2 = new Poll();
    //        poll2.setId(2);
    //        boolean sameHashCode = false;
    //        boolean differentHashCodeInitially = false;
    //
    //        while (poll1.hashCode() != poll2.hashCode()) {
    //            differentHashCodeInitially = true;
    //
    //            poll1 = new Poll();
    //            poll1.setId(1);
    //            poll2 = new Poll();
    //            poll2.setId(2);
    //
    //            if (poll1.hashCode() == poll2.hashCode()) {
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

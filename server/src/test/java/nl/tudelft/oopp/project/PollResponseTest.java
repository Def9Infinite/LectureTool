package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import nl.tudelft.oopp.project.entities.Poll;
import nl.tudelft.oopp.project.entities.PollOption;
import nl.tudelft.oopp.project.entities.PollResponse;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class PollResponseTest {

    private List<PollResponse> pollResponses;

    /**
     * Creates pollResponses before each test.
     */
    @BeforeEach
    public void createTestPollResponses() {
        pollResponses = new ArrayList<>();

        // pollResponse1
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

        String pollOptionText1 = "pollOptionText1";

        PollOption pollOption1 = new PollOption(pollOptionText1, poll1);
        pollOption1.setId(id1);

        PollResponse pollResponse1 = new PollResponse(poll1, user1, pollOption1);
        pollResponse1.setId(id1);

        pollResponses.add(pollResponse1);

        // pollResponse2 - equals to pollResponse1
        PollResponse pollResponse2 = new PollResponse(poll1, user1, pollOption1);
        pollResponse2.setId(id1);

        pollResponses.add(pollResponse2);

        // pollOption3 - different from pollOption1
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

        PollOption pollOption3 = new PollOption(pollOptionText1, poll3);
        pollOption3.setId(id3);

        PollResponse pollResponse3 = new PollResponse(poll3, user3, pollOption3);
        pollResponse3.setId(id3);

        pollResponses.add(pollResponse3);

        // pollResponse4 - equals to pollResponse1
        PollResponse pollResponse4 = new PollResponse(poll1, user1, pollOption1);
        pollResponse4.setId(id1);

        pollResponses.add(pollResponse4);

        // pollResponse5 - equals to pollResponse1
        PollResponse pollResponse5 = new PollResponse(poll1, user1, pollOption1);
        pollResponse5.setId(id1);

        pollResponses.add(pollResponse5);
    }

    @Test
    public void constructorTest1() {
        PollResponse pollResponse = new PollResponse();

        assertNotNull(pollResponse);
    }

    @Test
    public void constructorTest2() {
        Session session = new Session();
        User user = new User(UUID.randomUUID(), 1, "user", "ip", session);
        String pollText = "pollText";
        Poll poll = new Poll(session, pollText, user);

        String pollOptionText = "pollOptionText";

        PollOption pollOption = new PollOption(pollOptionText, poll);

        PollResponse pollResponse = new PollResponse(poll, user, pollOption);

        assertNotNull(pollResponse);
    }

    @Test
    public void setGetIdTest() {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(1);
        assertEquals(1, pollResponse.getId());
    }

    @Test
    public void setGetUserTest() {
        User user = new User();
        user.setNickname("user");

        PollResponse pollResponse = new PollResponse();
        pollResponse.setUser(user);

        assertEquals(user, pollResponse.getUser());
    }

    @Test
    public void setGetPollOptionTest() {
        Poll poll = new Poll();
        Session session = new Session();
        String sessionName = "sessionName";
        session.setSessionName(sessionName);
        poll.setSession(session);

        PollOption pollOption = new PollOption();
        pollOption.setPoll(poll);

        PollResponse pollResponse = new PollResponse();
        pollResponse.setPollOption(pollOption);

        assertEquals(pollOption, pollResponse.getPollOption());
    }

    @Test
    public void equalsReflexiveTest() {
        PollResponse pollResponse1 = pollResponses.get(0);
        assertEquals(pollResponse1, pollResponse1);
    }

    @Test
    public void equalsSameTest() {
        PollResponse pollResponse1 = pollResponses.get(0);
        PollResponse pollResponse2 = pollResponses.get(1);
        assertEquals(pollResponse1, pollResponse2);
    }

    @Test
    public void equalsSymmetricTest() {
        PollResponse pollResponse1 = pollResponses.get(0);
        PollResponse pollResponse2 = pollResponses.get(1);
        boolean equals1 = pollResponse1.equals(pollResponse2);
        boolean equals2 = pollResponse2.equals(pollResponse1);
        assertEquals(true, equals1 == equals2);
    }

    @Test
    public void equalsDifferentIdTest() {
        PollResponse pollResponse1 = pollResponses.get(0);
        PollResponse pollResponse2 = pollResponses.get(1);
        long newId = 2;
        pollResponse2.setId(newId);
        assertNotEquals(pollResponse1, pollResponse2);
    }

    @Test
    public void equalsDifferentUserTest() {
        PollResponse pollResponse1 = pollResponses.get(0);
        PollResponse pollResponse2 = pollResponses.get(1);
        User newUser = new User();
        newUser.setNickname("newUser");

        pollResponse2.setUser(newUser);

        assertNotEquals(pollResponse1, pollResponse2);
    }

    @Test
    public void equalsDifferentPollOptionTest() {
        PollResponse pollResponse1 = pollResponses.get(0);
        final PollResponse pollResponse2 = pollResponses.get(1);

        PollOption newPollOption1 = new PollOption();
        newPollOption1.setText("newPollOption1");
        PollOption newPollOption2 = new PollOption();
        newPollOption2.setText("newPollOption2");

        pollResponse1.setPollOption(newPollOption1);
        pollResponse2.setPollOption(newPollOption2);

        assertNotEquals(pollResponse1, pollResponse2);
    }

    @Test
    public void equalsTransitiveTest() {
        PollResponse pollResponse1 = pollResponses.get(0);
        PollResponse pollResponse2 = pollResponses.get(1);
        PollResponse pollResponse4 = pollResponses.get(3);
        boolean p1EqualsP2 = pollResponse1.equals(pollResponse2);
        boolean p2EqualsP4 = pollResponse2.equals(pollResponse4);
        boolean p1EqualsP4Actual = pollResponse1.equals(pollResponse4);
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
        PollResponse pollResponse1 = pollResponses.get(0);
        PollResponse pollResponse5 = pollResponses.get(4);
        boolean equals1 = pollResponse1.equals(pollResponse5);
        pollResponse5.setId(2);
        boolean equals2 = pollResponse1.equals(pollResponse5);

        assertNotEquals(equals1, equals2);
    }

    @Test
    public void hashCodeEqualsConsistentTest() {
        PollResponse pollResponse1 = pollResponses.get(0);
        PollResponse pollResponse5 = pollResponses.get(4);
        boolean equalsHashCode = pollResponse1.hashCode() == pollResponse5.hashCode();

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
    //        PollResponse pollResponse1 = new PollResponse();
    //        pollResponse1.setId(1);
    //        PollResponse pollResponse2 = new PollResponse();
    //        pollResponse2.setId(2);
    //        boolean sameHashCode = false;
    //        boolean differentHashCodeInitially = false;
    //
    //        while (pollResponse1.hashCode() != pollResponse2.hashCode()) {
    //            differentHashCodeInitially = true;
    //
    //            pollResponse1 = new PollResponse();
    //            pollResponse1.setId(1);
    //            pollResponse2 = new PollResponse();
    //            pollResponse2.setId(2);
    //
    //            if (pollResponse1.hashCode() == pollResponse2.hashCode()) {
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

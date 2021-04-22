package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import nl.tudelft.oopp.project.entities.Poll;
import nl.tudelft.oopp.project.entities.PollOption;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PollOptionTest {

    private List<PollOption> pollOptions;

    /**
     * Creates pollOptions before each test.
     */
    @BeforeEach
    public void createTestPollOptions() {
        pollOptions = new ArrayList<>();

        // pollOption1
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

        pollOptions.add(pollOption1);

        // pollOption2 - equals to pollOption1
        PollOption pollOption2 = new PollOption(pollOptionText1, poll1);
        pollOption2.setId(id1);

        pollOptions.add(pollOption2);

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

        pollOptions.add(pollOption3);

        // pollOption4 - equals to pollOption1
        PollOption pollOption4 = new PollOption(pollOptionText1, poll1);
        pollOption4.setId(id1);

        pollOptions.add(pollOption4);

        // pollOption5 - equals to pollOption1
        PollOption pollOption5 = new PollOption(pollOptionText1, poll1);
        pollOption5.setId(id1);

        pollOptions.add(pollOption5);
    }

    @Test
    public void constructorTest1() {
        PollOption pollOption = new PollOption();

        assertNotNull(pollOption);
    }

    @Test
    public void constructorTest2() {
        Session session = new Session();
        User user = new User(UUID.randomUUID(), 1, "user", "ip", session);
        String pollText = "pollText";
        Poll poll = new Poll(session, pollText, user);

        String pollOptionText = "pollOptionText";

        PollOption pollOption = new PollOption(pollOptionText, poll);

        assertNotNull(pollOption);
    }

    @Test
    public void setGetIdTest() {
        PollOption pollOption = new PollOption();
        pollOption.setId(1);
        assertEquals(1, pollOption.getId());
    }

    @Test
    public void setGetPollOptionTextTest() {
        String pollOptionText = "pollOptionText";

        PollOption pollOption = new PollOption();
        pollOption.setText(pollOptionText);

        assertEquals(pollOptionText, pollOption.getText());
    }

    @Test
    public void setGetPollTest() {
        Poll poll = new Poll();
        Session session = new Session();
        String sessionName = "sessionName";
        session.setSessionName(sessionName);
        poll.setSession(session);

        PollOption pollOption = new PollOption();
        pollOption.setPoll(poll);

        assertEquals(poll, pollOption.getPoll());
    }

    @Test
    public void equalsReflexiveTest() {
        PollOption pollOption1 = pollOptions.get(0);
        assertEquals(pollOption1, pollOption1);
    }

    @Test
    public void equalsSameTest() {
        PollOption pollOption1 = pollOptions.get(0);
        PollOption pollOption2 = pollOptions.get(1);
        assertEquals(pollOption1, pollOption2);
    }

    @Test
    public void equalsSymmetricTest() {
        PollOption pollOption1 = pollOptions.get(0);
        PollOption pollOption2 = pollOptions.get(1);
        boolean equals1 = pollOption1.equals(pollOption2);
        boolean equals2 = pollOption2.equals(pollOption1);
        assertEquals(true, equals1 == equals2);
    }

    @Test
    public void equalsDifferentIdTest() {
        PollOption pollOption1 = pollOptions.get(0);
        PollOption pollOption2 = pollOptions.get(1);
        long newId = 2;
        pollOption2.setId(newId);
        assertNotEquals(pollOption1, pollOption2);
    }

    @Test
    public void equalsDifferentPollOptionTextTest() {
        PollOption pollOption1 = pollOptions.get(0);
        PollOption pollOption2 = pollOptions.get(1);
        String newPollOptionText = "newPollOptionText";
        pollOption1.setText(newPollOptionText);

        assertNotEquals(pollOption1, pollOption2);
    }

    @Test
    public void equalsDifferentPollTest() {
        PollOption pollOption1 = pollOptions.get(0);
        PollOption pollOption2 = pollOptions.get(1);

        Poll newPoll = new Poll();
        newPoll.setId(2);

        pollOption2.setPoll(newPoll);

        assertNotEquals(pollOption1, pollOption2);
    }

    @Test
    public void equalsTransitiveTest() {
        PollOption pollOption1 = pollOptions.get(0);
        PollOption pollOption2 = pollOptions.get(1);
        PollOption pollOption4 = pollOptions.get(3);
        boolean p1EqualsP2 = pollOption1.equals(pollOption2);
        boolean p2EqualsP4 = pollOption2.equals(pollOption4);
        boolean p1EqualsP4Actual = pollOption1.equals(pollOption4);
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
        PollOption pollOption1 = pollOptions.get(0);
        PollOption pollOption5 = pollOptions.get(4);
        boolean equals1 = pollOption1.equals(pollOption5);
        pollOption5.setId(2);
        boolean equals2 = pollOption1.equals(pollOption5);

        assertNotEquals(equals1, equals2);
    }

    @Test
    public void hashCodeEqualsConsistentTest() {
        PollOption pollOption1 = pollOptions.get(0);
        PollOption pollOption5 = pollOptions.get(4);
        boolean equalsHashCode = pollOption1.hashCode() == pollOption5.hashCode();

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
    //        PollOption pollOption1 = new PollOption();
    //        pollOption1.setId(1);
    //        PollOption pollOption2 = new PollOption();
    //        pollOption2.setId(2);
    //        boolean sameHashCode = false;
    //        boolean differentHashCodeInitially = false;
    //
    //        while (pollOption1.hashCode() != pollOption2.hashCode()) {
    //            differentHashCodeInitially = true;
    //
    //            pollOption1 = new PollOption();
    //            pollOption1.setId(1);
    //            pollOption2 = new PollOption();
    //            pollOption2.setId(2);
    //
    //            if (pollOption1.hashCode() == pollOption2.hashCode()) {
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

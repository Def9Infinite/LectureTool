package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.project.entities.Question;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.Upvote;
import nl.tudelft.oopp.project.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpvoteTest {

    private List<Upvote> upvotes;

    /**
     * Creates upvotes before each test.
     */
    @BeforeEach
    public void createTestUpvotes() {
        upvotes = new ArrayList<>();

        // upvote1
        long id1 = 1;
        String text1 = "text1";
        String answer1 = "answer1";
        final boolean answerStatus1 = true;

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

        Question question1 = new Question(text1, user1, session1);
        question1.setAnswer(answer1);
        question1.setAnswerStatus(answerStatus1);

        Upvote upvote1 = new Upvote(question1, user1);
        upvote1.setId(1);

        upvotes.add(upvote1);


        // upvote2 - equals to upvote1
        Upvote upvote2 = new Upvote(question1, user1);
        upvote2.setId(1);

        upvotes.add(upvote2);

        // upvote3 - different from upvote1
        long id3 = 1;
        String text3 = "text1";
        String answer3 = "answer1";
        final boolean answerStatus3 = true;

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

        Question question3 = new Question(text3, user3, session3);
        question3.setAnswer(answer3);
        question3.setAnswerStatus(answerStatus3);

        Upvote upvote3 = new Upvote(question3, user3);
        upvote3.setId(id3);

        upvotes.add(upvote3);

        // upvote4 - equals to upvote1
        Upvote upvote4 = new Upvote(question1, user1);
        upvote4.setId(1);

        upvotes.add(upvote4);

        // upvote5 - equals to upvote1
        Upvote upvote5 = new Upvote(question1, user1);
        upvote5.setId(1);

        upvotes.add(upvote5);
    }

    @Test
    public void constructorTest1() {
        Upvote upvote = new Upvote();
        assertNotNull(upvote);
    }

    @Test
    public void constructorTest2() {
        Session session = new Session();
        User user = new User(UUID.randomUUID(), 1, "user", "ip", session);
        Question question = new Question("text", user, session);
        Upvote upvote = new Upvote(question, user);
        assertNotNull(upvote);
    }

    @Test
    public void setGetIdTest() {
        Upvote upvote = new Upvote();
        upvote.setId(1);
        assertEquals(1, upvote.getId());
    }

    @Test
    public void setGetQuestionTest() {
        Upvote upvote = new Upvote();
        Question question = new Question();
        String answer = "answer";
        question.setAnswer(answer);
        upvote.setQuestion(question);
        assertEquals(question, upvote.getQuestion());
    }

    @Test
    public void setGetUserTest() {
        Upvote upvote = new Upvote();
        User user = new User();
        String nickname = "user";
        user.setNickname(nickname);
        upvote.setUser(user);
        assertEquals(user, upvote.getUser());
    }

    @Test
    public void equalsReflexiveTest() {
        Upvote upvote1 = upvotes.get(0);
        assertEquals(upvote1, upvote1);
    }

    @Test
    public void equalsSameTest() {
        Upvote upvote1 = upvotes.get(0);
        Upvote upvote2 = upvotes.get(1);
        assertEquals(upvote1, upvote2);
    }

    @Test
    public void equalsSymmetricTest() {
        Upvote upvote1 = upvotes.get(0);
        Upvote upvote2 = upvotes.get(1);
        boolean equals1 = upvote1.equals(upvote2);
        boolean equals2 = upvote2.equals(upvote1);
        assertEquals(true, equals1 == equals2);
    }

    @Test
    public void equalsDifferentIdTest() {
        Upvote upvote1 = upvotes.get(0);
        Upvote upvote2 = upvotes.get(1);
        long newId = 2;
        upvote2.setId(newId);
        assertNotEquals(upvote1, upvote2);
    }

    @Test
    public void equalsDifferentUserTest() {
        Upvote upvote1 = upvotes.get(0);
        Upvote upvote2 = upvotes.get(1);
        User newUser = new User();
        newUser.setId(2);
        upvote2.setUser(newUser);

        assertNotEquals(upvote1, upvote2);
    }

    @Test
    public void equalsDifferentQuestionTest() {
        Upvote upvote1 = upvotes.get(0);
        Upvote upvote2 = upvotes.get(1);
        Question newQuestion = new Question();
        newQuestion.setId(2);
        upvote2.setQuestion(newQuestion);

        assertNotEquals(upvote1, upvote2);
    }

    @Test
    public void equalsTransitiveTest() {
        Upvote upvote1 = upvotes.get(0);
        Upvote upvote2 = upvotes.get(1);
        Upvote upvote4 = upvotes.get(3);
        boolean u1EqualsU2 = upvote1.equals(upvote2);
        boolean u2EqualsU4 = upvote2.equals(upvote4);
        boolean u1EqualsU4Actual = upvote1.equals(upvote4);
        boolean u1EqualsU4Theoretical;
        if (u1EqualsU2 == u2EqualsU4) {
            u1EqualsU4Theoretical = true;
        } else {
            u1EqualsU4Theoretical = false;
        }
        assertEquals(u1EqualsU4Actual, u1EqualsU4Theoretical);
    }

    @Test
    public void equalsConsistentTest() {
        Upvote upvote1 = upvotes.get(0);
        Upvote upvote5 = upvotes.get(4);
        boolean equals1 = upvote1.equals(upvote5);
        upvote5.setId(2);
        boolean equals2 = upvote1.equals(upvote5);

        assertNotEquals(equals1, equals2);
    }

    @Test
    public void equalsNullTest() {
        Upvote upvote1 = upvotes.get(0);
        Upvote upvote5 = null;
        assertEquals(false, upvote1.equals(upvote5));
    }

    @Test
    public void hashCodeEqualsConsistentTest() {
        Upvote upvote1 = upvotes.get(0);
        Upvote upvote5 = upvotes.get(4);
        boolean equalsHashCode = upvote1.hashCode() == upvote5.hashCode();

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
    //        Upvote upvote1 = new Upvote();
    //        upvote1.setId(1);
    //        Upvote upvote2 = new Upvote();
    //        upvote2.setId(2);
    //        boolean sameHashCode = false;
    //        boolean differentHashCodeInitially = false;
    //
    //        while (upvote1.hashCode() != upvote2.hashCode()) {
    //            differentHashCodeInitially = true;
    //
    //            upvote1 = new Upvote();
    //            upvote1.setId(1);
    //            upvote2 = new Upvote();
    //            upvote2.setId(2);
    //
    //            if (upvote1.hashCode() == upvote2.hashCode()) {
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

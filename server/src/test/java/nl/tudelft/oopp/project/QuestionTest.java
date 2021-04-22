package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.project.entities.Question;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;




public class QuestionTest {

    private List<Question> questions;

    /**
     * Creates questions before each test.
     */
    @BeforeEach
    public void createTestQuestions() {
        questions = new ArrayList<>();

        // question1
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
        questions.add(question1);

        // question2 - equals to question1
        Question question2 = new Question(text1, user1, session1);
        question2.setAnswer(answer1);
        question2.setAnswerStatus(answerStatus1);
        questions.add(question2);

        // question3 - different from question1
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
        Session session3 = new Session(powerToken1, studentToken1, sessionName1);
        session3.setId(3);
        User user3 = new User(token1, role1, nickname3, ipAddress3, session3);
        user3.setId(id3);

        Question question3 = new Question(text3, user3, session3);
        question3.setAnswer(answer3);
        question3.setAnswerStatus(answerStatus3);
        questions.add(question3);

        // question4 - equals to question1
        Question question4 = new Question(text1, user1, session1);
        question4.setAnswer(answer1);
        question4.setAnswerStatus(answerStatus1);
        questions.add(question4);

        // question5 - equals to question1
        Question question5 = new Question(text1, user1, session1);
        question5.setAnswer(answer1);
        question5.setAnswerStatus(answerStatus1);
        questions.add(question5);
    }

    @Test
    public void constructorTest1() {
        Question question = new Question();
        assertNotNull(question);
    }

    @Test
    public void constructorTest2() {
        Session session = new Session();
        User user = new User(UUID.randomUUID(), 1, "user", "ip", session);
        Question question = new Question("text", user, session);
        assertNotNull(question);
    }

    @Test
    public void setGetIdTest() {
        Question question = new Question();
        question.setId(1);
        assertEquals(1, question.getId());
    }

    @Test
    public void setGetAnswerTest() {
        Question question = new Question();
        String answer = "answer";
        question.setAnswer(answer);
        assertEquals(answer, question.getAnswer());
    }

    @Test
    public void setGetAnswerStatusTest() {
        Question question = new Question();
        boolean answerStatus = true;
        question.setAnswerStatus(answerStatus);
        assertEquals(answerStatus, question.getAnswerStatus());
    }

    @Test
    public void setGetUserTest() {
        Question question = new Question();

        User user = new User();
        String nickname = "user";
        user.setNickname(nickname);

        question.setUser(user);

        assertEquals(user, question.getUser());
    }

    @Test
    public void setGetSessionTest() {
        Question question = new Question();

        Session session = new Session();
        String sessionName = "session";
        session.setSessionName(sessionName);

        question.setSession(session);

        assertEquals(session, question.getSession());
    }

    @Test
    public void equalsReflexiveTest() {
        Question question1 = questions.get(0);
        assertEquals(question1, question1);
    }

    @Test
    public void equalsSameTest() {
        Question question1 = questions.get(0);
        Question question2 = questions.get(1);
        assertEquals(question1, question2);
    }

    @Test
    public void equalsSymmetricTest() {
        Question question1 = questions.get(0);
        Question question2 = questions.get(1);
        boolean equals1 = question1.equals(question2);
        boolean equals2 = question2.equals(question1);
        assertEquals(true, equals1 == equals2);
    }

    @Test
    public void equalsDifferentIdTest() {
        Question question1 = questions.get(0);
        Question question2 = questions.get(1);
        long newId = 2;
        question2.setId(newId);
        assertNotEquals(question1, question2);
    }

    @Test
    public void equalsDifferentTextTest() {
        Question question1 = questions.get(0);
        Question question2 = questions.get(1);
        String newText = "newText";
        question2.setText(newText);
        assertNotEquals(question1, question2);
    }

    @Test
    public void equalsDifferentAnswerTest() {
        Question question1 = questions.get(0);
        Question question2 = questions.get(1);
        String newAnswer = "newAnswer";
        question2.setAnswer(newAnswer);
        assertNotEquals(question1, question2);
    }

    @Test
    public void equalsDifferentAnswerStatusTest() {
        Question question1 = questions.get(0);
        Question question2 = questions.get(1);
        boolean newAnswerStatus = true;
        boolean answerStatus1 = question1.getAnswerStatus();
        if (answerStatus1) {
            newAnswerStatus = false;
        }
        question2.setAnswerStatus(newAnswerStatus);
        assertNotEquals(question1, question2);
    }

    @Test
    public void equalsDifferentUserTest() {
        Question question1 = questions.get(0);
        Question question2 = questions.get(1);
        User newUser = new User();
        newUser.setId(2);
        question2.setUser(newUser);

        assertNotEquals(question1, question2);
    }

    @Test
    public void equalsDifferentSessionTest() {
        Question question1 = questions.get(0);
        Question question2 = questions.get(1);
        Session newSession = new Session();
        newSession.setId(2);
        question2.setSession(newSession);

        assertNotEquals(question1, question2);
    }

    @Test
    public void equalsTransitiveTest() {
        Question question1 = questions.get(0);
        Question question2 = questions.get(1);
        Question question4 = questions.get(3);
        boolean q1EqualsQ2 = question1.equals(question2);
        boolean q2EqualsQ4 = question2.equals(question4);
        boolean q1EqualsQ4Actual = question1.equals(question4);
        boolean q1EqualsQ4Theoretical;
        if (q1EqualsQ2 == q2EqualsQ4) {
            q1EqualsQ4Theoretical = true;
        } else {
            q1EqualsQ4Theoretical = false;
        }
        assertEquals(q1EqualsQ4Actual, q1EqualsQ4Theoretical);
    }

    @Test
    public void equalsConsistentTest() {
        Question question1 = questions.get(0);
        Question question5 = questions.get(4);
        boolean equals1 = question1.equals(question5);
        question5.setId(2);
        boolean equals2 = question1.equals(question5);

        assertNotEquals(equals1, equals2);
    }

    @Test
    public void hashCodeEqualsConsistentTest() {
        Question question1 = questions.get(0);
        Question question5 = questions.get(4);
        boolean equalsHashCode = question1.hashCode() == question5.hashCode();

        assertEquals(true, equalsHashCode);
    }

    @Test
    public void equalsNullTest() {
        Question question1 = questions.get(0);
        Question question5 = null;

        assertFalse(question1.equals(question5));
    }

    //            /**
    //             * This test takes longer than usual, depending on how fast two new
    //             * different objects get the same hash code.
    //             * The two objects are different, because their sessions are different.
    //             * In theory there should be 2 different objects with the same hashCode.
    //             * In practice, the test does not find them.
    //             */
    //        @Test
    //        public void hashCodeCollisionsTest() {
    //            Question question1 = new Question();
    //            question1.setId(1);
    //            Question question2 = new Question();
    //            question2.setId(2);
    //            boolean sameHashCode = false;
    //            boolean differentHashCodeInitially = false;
    //
    //            while (question1.hashCode() != question2.hashCode()) {
    //                differentHashCodeInitially = true;
    //
    //                question1 = new Question();
    //                question1.setId(1);
    //                question2 = new Question();
    //                question2.setId(2);
    //
    //                if (question1.hashCode() == question2.hashCode()) {
    //                    sameHashCode = true;
    //                }
    //            }
    //
    //            if (!differentHashCodeInitially) {
    //                sameHashCode = true;
    //            }
    //
    //            assertEquals(true, sameHashCode);
    //        }
}

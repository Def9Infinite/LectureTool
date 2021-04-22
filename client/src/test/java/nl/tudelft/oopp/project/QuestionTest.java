package nl.tudelft.oopp.project;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    @Test
    public void constructorTest() {
        Question question = new Question("text");
        Assertions.assertEquals("text",question.getText());
    }

    @Test
    public void doubleQuestionEquals() {
        Question question = new Question("Text");
        Question question1 = question;
        Assertions.assertTrue(question.equals(question1));
    }

    @Test
    public void nullQuestionEquals() {
        Question question = new Question("Text");
        Question question1 = null;
        Assertions.assertFalse(question.equals(question1));
    }

    @Test
    public void hashTest() {
        Question question = new Question("Text");
        Question question1 = new Question("Text");
        Assertions.assertTrue(question.hashCode() == question1.hashCode());
    }

    @Test
    public void isUpvotedByUser() {
        List<Upvote> upvoteList = new ArrayList<>();
        User user = new User("Mirko");
        Question question = new Question(1, "Hey",user, upvoteList);
        Assertions.assertFalse(question.isUpvotedByUser(user));
    }

    @Test
    public void isUpvotedByUserTrue() {
        List<Upvote> upvoteList = new ArrayList<>();
        User user = new User("Mirko");
        Upvote upvote = new Upvote(new Question("Test"),user);
        upvoteList.add(upvote);
        Question question = new Question(1, "Hey",user, upvoteList);
        Assertions.assertTrue(question.isUpvotedByUser(user));
    }

    @Test
    public void getNumUpvotes() {
        List<Upvote> upvoteList = new ArrayList<>();
        User user = new User("Mirko");
        Question question = new Question(1, "Hey",user, upvoteList);
        Assertions.assertEquals(0,question.getUpvoteCount());
    }

    @Test
    public void getNumUpvotesNull() {
        Question question = new Question("text");
        Assertions.assertEquals(0,question.getUpvoteCount());
    }

    @Test
    public void getId() {
        List<Upvote> upvoteList = new ArrayList<>();
        User user = new User("Mirko");
        Question question = new Question(1, "Hey",user, upvoteList);
        Assertions.assertEquals(1,question.getId());
    }

    @Test
    public void setGetAnswer() {
        Question question = new Question("text");
        question.setAnswer("answer");
        Assertions.assertEquals("answer",question.getAnswer());
    }

    @Test
    public void isAnswered() {
        Question question = new Question("Text");
        Assertions.assertEquals(false,question.isAnswered());
    }

}

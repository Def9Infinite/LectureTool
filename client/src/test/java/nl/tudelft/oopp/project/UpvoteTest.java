package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UpvoteTest {

    private static User user = new User("Mirko");

    @Test
    public void constructor() {
        Upvote upvote = new Upvote(1,new Question("text"),user);
    }

    @Test
    public void getSetId() {
        Upvote upvote = new Upvote(1,new Question("text"),user);
        upvote.setId(5);
        assertEquals(5,upvote.getId());
    }

    @Test
    public void getSetQuestion() {
        Upvote upvote = new Upvote(1,new Question("text"),user);
        Question question = new Question("Test");
        upvote.setQuestion(question);
        assertEquals(question,upvote.getQuestion());
    }

    @Test
    public void getSetUser() {
        User u = new User("Dave");
        Upvote upvote = new Upvote(1,new Question("text"),user);
        upvote.setUser(u);
        assertEquals(u,upvote.getUser());
    }

    @Test
    public void equals() {
        Upvote upvote = new Upvote(1,new Question("text"),user);
        Upvote upvote1 = upvote;
        assertTrue(upvote.equals(upvote1));
    }

    @Test
    public void equalsNull() {
        Upvote upvote = new Upvote(1,new Question("text"),user);
        Upvote upvote1 = null;
        assertFalse(upvote.equals(upvote1));
    }

    @Test
    public void equalsFalse() {
        Upvote upvote = new Upvote(1,new Question("text"),user);
        Upvote upvote2 = new Upvote(3,new Question("text"),user);
        assertFalse(upvote.equals(upvote2));
    }

    @Test
    public void hash() {
        Upvote upvote = new Upvote(1,new Question("text"),user);
        assertNotNull(upvote.hashCode());
    }

    @Test
    public void toStringTest() {
        Question question = new Question("text");
        Upvote upvote = new Upvote(1,new Question("text"),user);
        assertEquals("Upvote{id=1, question=" + question.toString()
                + ", user=userId=0, token=null, role=0, nickname='Mirko'}",upvote.toString());
    }



}

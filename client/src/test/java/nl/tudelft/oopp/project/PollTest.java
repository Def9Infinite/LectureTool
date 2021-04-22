package nl.tudelft.oopp.project;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class PollTest {

    @Test
    public void pollOptionConstructor() {
        Poll.PollOption option = new Poll.PollOption("Answer a");
    }

    @Test
    public void optionGetId() {
        Poll.PollOption option = new Poll.PollOption("fsgs");
        Assertions.assertEquals(0,option.getId());
    }

    @Test
    public void optionGetText() {
        Poll.PollOption option = new Poll.PollOption("Answer a");
        Assertions.assertEquals("Answer a", option.getText());
    }

    @Test
    public void pollConstructor() {
        List<String> options = new ArrayList<>();
        options.add("Answer a");
        options.add("Answer b");
        Poll poll = new Poll("Question",options);
    }

    @Test
    public void getOpen() {
        List<String> options = new ArrayList<>();
        Poll poll = new Poll("Question",options);
        Assertions.assertEquals(false,poll.getOpen());
    }

    @Test
    public void getQuestion() {
        List<String> options = new ArrayList<>();
        Poll poll = new Poll("Question",options);
        Assertions.assertEquals("Question", poll.getQuestion());
    }

    @Test
    public void getOptions() {
        List<String> options = new ArrayList<>();
        options.add("Answer a");
        options.add("Answer b");
        Poll poll = new Poll("Question",options);
        Assertions.assertEquals(options.get(0),poll.getPollOptions().get(0).getText());
    }

    @Test
    public void getIdTest() {
        List<String> options = new ArrayList<>();
        options.add("Answer a");
        options.add("Answer b");
        Poll poll = new Poll("Question",options);
        Assertions.assertEquals(0,poll.getId());
    }

    @Test
    public void pollOptionVote() {
        Poll.PollOption option = new Poll.PollOption("Answer a");
        Assertions.assertEquals(0,option.getVoteCount());
    }

    @Test
    public void pollGetVotes() {
        List<String> options = new ArrayList<>();
        options.add("Answer a");
        options.add("Answer b");
        Poll poll = new Poll("Question",options);
        Assertions.assertEquals(0,poll.getTotalVotes());
    }
}

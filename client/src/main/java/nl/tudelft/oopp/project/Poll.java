package nl.tudelft.oopp.project;

import java.util.ArrayList;
import java.util.List;

public class Poll {
    public static class PollOption {
        private String text;
        private long id;
        private long voteCount;

        PollOption(String text) {
            this.text = text;
        }

        public long getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public long getVoteCount() {
            return voteCount;
        }
    }

    private String question;
    private List<PollOption> pollOptions;
    private boolean open;
    private long id;

    /**
     * Creates polls.
     * @param question poll question text
     * @param answerOptions converted to PollOptions
     */
    public Poll(String question, List<String> answerOptions) {
        this.question = question;
        this.pollOptions = new ArrayList<>();
        for (String a : answerOptions) {
            this.pollOptions.add(new PollOption(a));
        }
    }

    public long getTotalVotes() {
        return this.pollOptions.stream().mapToLong(PollOption::getVoteCount).sum();
    }


    public boolean getOpen() {
        return open;
    }

    public String getQuestion() {
        return question;
    }

    public List<PollOption> getPollOptions() {
        return pollOptions;
    }

    public long getId() {
        return id;
    }

}

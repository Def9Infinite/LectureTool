package nl.tudelft.oopp.project.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.ArrayList;
import java.util.List;

public class PollDto {
    public static class PollOptionDto {
        public String text;
        public long id;
        public long voteCount;

        /**
         * Constructor based on DB entity.
         * @param pollOption base entity.
         */
        public PollOptionDto(PollOption pollOption) {
            this.text = pollOption.getText();
            this.id = pollOption.getId();
            this.voteCount = pollOption.getVoteCount();
        }
    }

    public String question;
    public List<PollOptionDto> pollOptions;
    public boolean open;
    public long id;

    /**
     * Constructor based on DB entity.
     * @param poll base entity.
     */
    public PollDto(Poll poll) {
        this.id = poll.getId();
        this.question = poll.getQuestion();
        this.open = poll.getOpen();
        this.pollOptions = new ArrayList<>();
        for (PollOption po : poll.getPollOptions()) {
            this.pollOptions.add(new PollOptionDto(po));
        }
    }

    public List<PollOptionDto> getPollOptions() {
        return pollOptions;
    }
}

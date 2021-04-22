package nl.tudelft.oopp.project.entities;

import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * An entity regarding to the option of each poll.
 *
 * @pollOptionId the unique ID for each option
 * @pollOptionText the text for each option
 * @pollId for which poll is this option created
 */
@Entity
@Table(name = "poll_option")
public class PollOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    private Poll poll;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "poll_option_id")
    private Set<PollResponse> votes;

    public PollOption() {

    }

    /**
     * construct the poll option.
     *
     * @param text the text for each option
     * @param poll           the poll it relates to
     */
    public PollOption(String text, Poll poll) {
        this.text = text;
        this.poll = poll;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String pollOptionText) {
        this.text = pollOptionText;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    /**
     * Returns vote count.
     * @return vote count
     */
    public long getVoteCount() {
        if (votes == null) {
            return 0;
        } else {
            return votes.size();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PollOption that = (PollOption) o;
        return id == that.id && text.equals(that.text)
                && poll.equals(that.poll);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, poll);
    }
}

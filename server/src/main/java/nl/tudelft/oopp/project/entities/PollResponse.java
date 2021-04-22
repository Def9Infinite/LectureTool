package nl.tudelft.oopp.project.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created poll response entity.
 *
 * @pollId which poll this response to
 * @userId who created this response
 * @pollOptionId which poll option this response to
 */
@Entity
@Table(name = "poll_response")
public class PollResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private PollOption pollOption;

    public PollResponse() {

    }

    /**
     * construct a response when created.
     *
     * @param poll       which poll does this response to
     * @param user       who created this response
     * @param pollOption which poll option this response response to
     */
    public PollResponse(Poll poll, User user, PollOption pollOption) {
        this.id = poll.getId();
        this.user = user;
        this.pollOption = pollOption;
    }

    public PollResponse(User user, PollOption pollOption) {
        this.user = user;
        this.pollOption = pollOption;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PollOption getPollOption() {
        return pollOption;
    }

    public void setPollOption(PollOption pollOption) {
        this.pollOption = pollOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PollResponse that = (PollResponse) o;
        return id == that.id && user.equals(that.user)
                && pollOption.equals(that.pollOption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, pollOption);
    }
}

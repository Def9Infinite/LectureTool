package nl.tudelft.oopp.project.entities;

import java.util.List;
import java.util.Objects;
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
 * Create entity about poll.
 *
 * @pollId unique ID for each poll created.
 * @sessionId for which session is this poll created.
 * @pollText the text for the poll.
 * @userId who created this poll.
 */
@Entity
@Table(name = "poll")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(name = "question")
    private String question;

    @Column(name = "open")
    private Boolean open = true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "poll_id")
    private List<PollOption> pollOptions;

    public Poll() {

    }

    /**
     * construct the poll entity.
     *
     * @param session  which session is this poll in
     * @param question text for poll
     * @param user     who created this poll
     */
    public Poll(Session session, String question, User user) {
        this.session = session;
        this.question = question;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String pollText) {
        this.question = pollText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public List<PollOption> getPollOptions() {
        return pollOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Poll poll = (Poll) o;
        return id == poll.id && session.equals(poll.session)
                && question.equals(poll.question) && user.equals(poll.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, session, question, user);
    }
}

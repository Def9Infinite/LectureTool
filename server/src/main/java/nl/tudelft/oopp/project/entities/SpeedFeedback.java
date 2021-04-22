package nl.tudelft.oopp.project.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The feedback toward session.
 *
 * @sessionId which session this feedback is for
 * @feedbackId the unique feedback id
 */
@Entity
@Table(name = "SpeedFeedback")
public class SpeedFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "speed_feedback")
    private int speedFeedback;

    public SpeedFeedback() {

    }


    /**
     * Constructor about this type.
     * @param session the session this is in
     * @param user who give this feedback
     * @param speed whats his/her feedback
     */
    public SpeedFeedback(Session session, User user, int speed) {
        this.session = session;
        this.user = user;
        this.speedFeedback = speed;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getSpeedFeedback() {
        return speedFeedback;
    }

    public void setSpeedFeedback(int speedFeedback) {
        this.speedFeedback = speedFeedback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpeedFeedback that = (SpeedFeedback) o;
        return id == that.id && Float.compare(that.speedFeedback, speedFeedback) == 0
                && session.equals(that.session) && user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, session, user, speedFeedback);
    }
}

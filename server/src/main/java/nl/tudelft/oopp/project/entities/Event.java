package nl.tudelft.oopp.project.entities;

import java.sql.Timestamp;
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
 * Log the event happened during each session.
 * @sessionId which session does this event happens in.
 * @pollId which poll is created.
 * @pollOptionId which poll option is created.
 * @pollResponseId whose poll response is created.
 * @questionId which question is created.
 * @upvoteId which question is getting upvote.
 * @SpeedFeedback who given a speed feedback.
 * @timestamp current timestamp of the event
 */
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "event_String")
    private String event;

    public Event() {

    }

    /**
     * Constructor.
     * @param session session this event belongs to
     * @param timestamp time of the event
     * @param event string representation of the event
     */
    public Event(Session session, Timestamp timestamp, String event) {
        this.session = session;
        this.timestamp = timestamp;
        this.event = event;
    }

    public String toLog() {
        return String.format(event + " on: " + timestamp + "\n");
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event1 = (Event) o;
        return getId() == event1.getId()
                && getSession().equals(event1.getSession())
                && Objects.equals(getTimestamp(), event1.getTimestamp())
                && Objects.equals(getEvent(), event1.getEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSession(), getTimestamp(), getEvent());
    }
}


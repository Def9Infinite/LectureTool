package nl.tudelft.oopp.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The Entity for each upvote.
 *
 * @questionId which question this upvote too
 * @userId who upvote the question
 */
@Entity
@Table(name = "upvote", uniqueConstraints = @UniqueConstraint(columnNames = {"question_id", "user_id"}))
public class Upvote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public Upvote() {
    }

    /**
     * Construct upvote when created.
     *
     * @param question of which this upload is related to
     * @param user     who upvote this question
     */
    public Upvote(Question question, User user) {
        this.question = question;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Upvote upvote = (Upvote) o;
        return id == upvote.id && question.equals(upvote.question) && user.equals(upvote.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, user);
    }
}

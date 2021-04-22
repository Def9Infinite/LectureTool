package nl.tudelft.oopp.project.entities;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
import javax.persistence.PrePersist;
import javax.persistence.Table;


/**
 * The Entity for question.
 *
 * @questionId unique identifier for each question
 * @text the text for the question
 * @answer the answer to the question if answered
 * @userId who asked this question
 * @sessionId in which session is this question asked
 */
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "text")
    private String text;

    @Column(name = "answer")
    private String answer;

    @Column(name = "answer_status")
    private boolean answerStatus;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "session_id")
    private Session session;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private Set<Upvote> upvotes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public Question() {

    }

    /**
     * Construct the question for each question created. By default, no answer is given.
     *
     * @param text       the main body the the question
     * @param user     the user who ask this question
     * @param session    in which session is this question asked
     */
    public Question(String text, User user, Session session) {
        this.text = text;
        this.answer = null;
        this.answerStatus = false;
        this.user = user;
        this.session = session;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(boolean answerStatus) {
        this.answerStatus = answerStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Set<Upvote> getUpvotes() {
        return upvotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setUpvotes(Set<Upvote> upvotes) {
        this.upvotes = upvotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return id == question.id && answerStatus == question.answerStatus
                && text.equals(question.text) && answer.equals(question.answer)
                && user.equals(question.user) && session.equals(question.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, answer, answerStatus, user, session);
    }

    public void setText(String text) {
        this.text = text;
    }
}

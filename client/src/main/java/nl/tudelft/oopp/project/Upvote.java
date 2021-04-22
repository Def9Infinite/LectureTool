package nl.tudelft.oopp.project;

import java.util.Objects;

public class Upvote {
    private long id;
    private Question question;
    private User user;


    public Upvote() {

    }

    /**
     * Constructor for User, creates a User object.
     * @param id - long, a unique number
     * @param question - Question, a question for which the upvote holds
     * @param user - User, a User who made the Upvote
     */
    public Upvote(long id, Question question, User user) {
        this.id = id;
        this.question = question;
        this.user = user;
    }

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
    public String toString() {
        return "Upvote{" + "id=" + id + ", question=" + question
                + ", user=" + user + '}';
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
        return id == upvote.id && user.equals(upvote.user) && question != null && question.equals(upvote.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, user);
    }
}

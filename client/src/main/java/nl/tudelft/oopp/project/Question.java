package nl.tudelft.oopp.project;

import java.util.List;
import java.util.Objects;

public class Question {
    private long id;
    private String text;
    private String answer;
    private boolean answerStatus;
    private User user;
    private List<Upvote> upvotes;

    /**
     * Creates a question object.
     * @param id question id
     * @param text question text
     * @param answer question answer text
     * @param answerStatus is the question answered?
     */
    public Question(long id, String text, String answer, boolean answerStatus, User user) {
        this.id = id;
        this.text = text;
        this.answer = answer;
        this.answerStatus = answerStatus;
        this.user = user;

    }

    /**
     * Constructor testing purposes.
     * @param id question id
     * @param text question text
     * @param user question user
     * @param upvotes upvote list
     */
    public Question(long id, String text, User user, List<Upvote> upvotes) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.upvotes = upvotes;
    }

    public Question(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    /**
     * Get upvotes count.
     * @return Upvote count or 0 if null.
     */
    public long getUpvoteCount() {
        if (upvotes == null) {
            return 0;
        }
        return upvotes.size();
    }

    /**
     * Check if question is upvoted by this user.
     * @param user user
     * @return true/false
     */
    public boolean isUpvotedByUser(User user) {
        for (Upvote u : upvotes) {
            if (u.getUser().getId() == user.getId()) {
                return true;
            }
        }
        return false;
    }

    public long getId() {
        return id;
    }

    public boolean isAnswered() {
        return answerStatus;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
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

        boolean easyChecks = id == question.id
                && answerStatus == question.answerStatus
                && Objects.equals(getText(), question.getText())
                && Objects.equals(answer, question.answer)
                && getUser().equals(question.getUser())
                && getUpvoteCount() == question.getUpvoteCount();
        if (!easyChecks) {
            return false;
        } else if (question.upvotes == null || upvotes == null) {
            return question.upvotes == null && upvotes == null;
        }
        // Only works if sorted which they ~probably~ are
        return (question.upvotes.equals(upvotes));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getText(), answer, answerStatus, getUser());
    }
}


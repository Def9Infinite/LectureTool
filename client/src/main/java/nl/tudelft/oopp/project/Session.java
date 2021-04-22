package nl.tudelft.oopp.project;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import nl.tudelft.oopp.project.exceptions.NoExistingSessionInstance;

/**
 * The session entity.
 * @sessionId the unique id for each session
 * @powerToken the token toward power user: professor/moderator
 * @studentToken the token toward the student
 */

public class Session {

    private long id;
    private UUID powerToken;
    private UUID studentToken;
    private String sessionName;
    private String date;

    private static Session instance;

    /**
     * Construct session when created.
     * @param id for the current session
     * @param powerToken the token for power_user (moderator/professor)
     * @param studentToken the token for student
     */
    public Session(long id, UUID powerToken, UUID studentToken,String sessionName) {
        this.id = id;
        this.powerToken = powerToken;
        this.studentToken = studentToken;
        this.sessionName = sessionName;
    }

    public Session(String sessionName, String date) {
        this.sessionName = sessionName;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public UUID getPowerToken() {
        return powerToken;
    }

    public UUID getStudentToken() {
        return studentToken;
    }

    public String getSessionName() {
        return sessionName;
    }

    public static void setInstance(Session instance) {
        Session.instance = instance;
    }

    public String getDate() {
        return date;
    }

    /**
     * Get instance of current session.
     * @return the current session
     * @throws NoExistingSessionInstance if current session does not exist
     */
    public static Session getInstance() throws NoExistingSessionInstance {
        if (instance == null) {
            throw new NoExistingSessionInstance();
        }
        return instance;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    @Override
    public String toString() {
        return "Session{ id=" + id + ", powerToken=" + powerToken + ", studentToken="
                + studentToken + ", sessionName=" + sessionName + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return getId() == session.getId()
                && Objects.equals(getPowerToken(), session.getPowerToken())
                && Objects.equals(getStudentToken(), session.getStudentToken())
                && Objects.equals(getSessionName(), session.getSessionName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPowerToken(), getStudentToken(), getSessionName());
    }
}

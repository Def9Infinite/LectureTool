package nl.tudelft.oopp.project;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import nl.tudelft.oopp.project.exceptions.NoExistingUserInstance;


/**
 * Creating field for the user field.
 *
 * @id User's id
 * @token User's token for this session (as it would change for different session)
 * @role User's role(0 -> students, 1-> professor/moderator)
 * @Nickname User's Nickname (maybe it will be save?)
 * @ipAddress Save the most recent IP address use by the User.
 */

public class User {

    private long id;
    private UUID token;
    private int role;
    private String nickname;
    private long sessionId;
    private UUID sessionToken;
    private Set<Long> pollResponseIds;

    private static User instance;

    /**
     * When a new user connect to the server.
     *
     * @param id    the unique id for the server
     * @param token     the token this id has
     * @param role      the role this user has
     * @param nickname  the nickname this user set
     */
    public User(long id, UUID token, int role, String nickname, Session session) {
        this.id = id;
        this.token = token;
        this.role = role;
        this.nickname = nickname;
        this.sessionId = session.getId();
        pollResponseIds = new HashSet<>();
    }

    /**
     * Constructor without id.
     * @param token     the token this id has
     * @param role      the role this user has
     * @param nickname  the nickname this user set
     */
    public User(UUID token, int role, String nickname, Session session) {
        this.token = token;
        this.role = role;
        this.nickname = nickname;
        this.sessionId = session.getId();
        pollResponseIds = new HashSet<>();
    }

    /**
     * Get polls response ids.
     * @return set
     */
    public Set<Long> getPollResponseIds() {
        if (pollResponseIds == null) {
            pollResponseIds = new HashSet<>();
        }
        return pollResponseIds;
    }

    public User(String nickname) {
        this.nickname = nickname;
    }

    public static void setInstance(User u) {
        instance = u;
    }

    public long getId() {
        return id;
    }

    public UUID getToken() {
        return token;
    }

    public int getRole() {
        return role;
    }

    public String getNickname() {
        return nickname;
    }

    public UUID getSessionToken() {
        return sessionToken;
    }

    public long getSessionId() {
        return sessionId;
    }

    /**
     * Get the existing user instance.
     * @return Returns the current user instance if it already exists.
     * @throws NoExistingUserInstance if no user instance exists.
     */
    public static User getInstance() throws NoExistingUserInstance {
        if (instance == null) {
            throw new NoExistingUserInstance();
        }
        return instance;
    }

    @Override
    public String toString() {
        return "userId=" + id + ", token=" + token + ", role=" + role
                + ", nickname='" + nickname + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id
                && getRole() == user.getRole()
                && sessionId == user.sessionId
                && Objects.equals(getToken(), user.getToken())
                && Objects.equals(getNickname(), user.getNickname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getToken(), getRole(), getNickname(), sessionId);
    }
}

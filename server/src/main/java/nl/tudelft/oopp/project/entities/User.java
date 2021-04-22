package nl.tudelft.oopp.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Creating field for the user field.
 *
 * @id User's id
 * @token User's token for this session (as it would change for different session)
 * @role User's role(0 -> students, 1-> professor/moderator)
 * @Nickname User's Nickname (maybe it will be save?)
 * @ipAddress Save the most recent IP address use by the User.
 */
@Entity
@Table(name = "client_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "token")
    private UUID token;

    @Column(name = "role")
    private int role;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "ip_address")
    private String ipAddress;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JsonIgnore
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(name = "last_action_timestamp")
    private double lastActionTimestamp;

    public User() {

    }

    public User(String nickname) {
        this.nickname = nickname;
    }

    /**
     * When a new user connect to the server.
     *
     * @param token     the token this id has
     * @param role      the role this user has
     * @param nickname  the nickname this user set
     * @param session   the session for the user
     */
    public User(UUID token, int role, String nickname, Session session) {
        this.token = token;
        this.role = role;
        this.nickname = nickname;
        this.session = session;
    }

    /**
     * When a new user connect to the server.
     *
     * @param token     the token this id has
     * @param role      the role this user has
     * @param nickname  the nickname this user set
     * @param ipAddress the login ipaddress this user is on
     * @param session   the session for the user
     */
    public User(UUID token, int role, String nickname, String ipAddress, Session session) {
        this.token = token;
        this.role = role;
        this.nickname = nickname;
        this.ipAddress = ipAddress;
        this.session = session;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public double getLastActionTimestamp() {
        return lastActionTimestamp;
    }

    public void setLastActionTimestamp(double lastActionTimestamp) {
        this.lastActionTimestamp = lastActionTimestamp;
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
        return id == user.id && role == user.role
                && token.equals(user.token) && nickname.equals(user.nickname)
                && ipAddress.equals(user.ipAddress) && session.equals(user.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, role, nickname, ipAddress, session);
    }
}

package nl.tudelft.oopp.project.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.PrePersist;
import javax.persistence.Table;


/**
 * The session entity.
 *
 * @id the unique id for each session
 * @powerToken the token toward power user: professor/moderator
 * @studentToken the token toward the student
 * @S
 */
@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "power_token")
    private UUID powerToken;
    @Column(name = "student_token")
    private UUID studentToken;

    @Column(name = "session_name")
    private String sessionName;

    @Column(name = "closed", nullable = false)
    private Boolean closed = false;

    @Column(name = "date")
    private String date;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "banned_ips", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "ip", nullable = false)
    private Set<String> bannedIps;

    @PrePersist
    public void prePersist() {
        powerToken = UUID.randomUUID();
        studentToken = UUID.randomUUID();
    }


    public Session(){

    }

    /**
     * Construct session when created.
     * @param powerToken   the token for power_user (moderator/professor)
     * @param studentToken the token for student
     */
    public Session(UUID powerToken, UUID studentToken,String sessionName) {
        this.powerToken = powerToken;
        this.studentToken = studentToken;
        this.sessionName = sessionName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getPowerToken() {
        return powerToken;
    }

    public void setPowerToken(UUID powerToken) {
        this.powerToken = powerToken;
    }

    public UUID getStudentToken() {
        return studentToken;
    }

    public void setStudentToken(UUID studentToken) {
        this.studentToken = studentToken;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    /**
     * True if IP is banned.
     * @param ip ip to check
     * @return true if IP is banned.
     */
    public boolean ipIsBanned(String ip) {
        if (bannedIps == null) {
            return false;
        }
        return bannedIps.contains(ip);
    }

    /**
     * Bans the ip!.
     * @param ip Ip to ban.
     */
    public void banIp(String ip) {
        if (bannedIps == null) {
            bannedIps = new HashSet<>();
        }
        bannedIps.add(ip);
    }

    public Set<String> getBannedIps() {
        return bannedIps;
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
        return id == session.id && powerToken.equals(session.powerToken)
                && studentToken.equals(session.studentToken)
                && sessionName.equals(session.sessionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, powerToken, studentToken, sessionName);
    }
}

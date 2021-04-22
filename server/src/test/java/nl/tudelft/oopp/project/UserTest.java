package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private List<User> users;

    /**
     * Creates users before each test.
     */
    @BeforeEach
    public void createTestUsers() {
        users = new ArrayList<>();

        // user1
        long id1 = 1;
        UUID token1 = UUID.randomUUID();
        int role1 = 1;
        String nickname1 = "user1";
        String ipAddress1 = "ip1";
        UUID powerToken1 = UUID.randomUUID();
        UUID studentToken1 = UUID.randomUUID();
        String sessionName1 = "session1";
        Session session1 = new Session(powerToken1, studentToken1, sessionName1);
        session1.setId(1);
        User user1 = new User(token1, role1, nickname1, ipAddress1, session1);
        user1.setId(id1);
        users.add(user1);

        // user2 - equals to user1
        User user2 = new User(token1, role1, nickname1, ipAddress1, session1);
        user2.setId(id1);
        users.add(user2);

        // user3 - different from user1
        long id3 = 3;
        UUID token3 = UUID.randomUUID();
        int role3 = 1;
        String nickname3 = "user1";
        String ipAddress3 = "ip1";
        UUID powerToken3 = UUID.randomUUID();
        UUID studentToken3 = UUID.randomUUID();
        String sessionName3 = "session3";
        Session session3 = new Session(powerToken1, studentToken1, sessionName1);
        session3.setId(3);
        User user3 = new User(token1, role1, nickname3, ipAddress3, session3);
        user3.setId(id3);
        users.add(user3);

        // user4 - equals to user1
        User user4 = new User(token1, role1, nickname1, ipAddress1, session1);
        user4.setId(id1);
        users.add(user4);

        // user5 - equals to user1
        User user5 = new User(token1, role1, nickname1, ipAddress1, session1);
        user5.setId(id1);
        users.add(user5);
    }

    @Test
    public void constructorTest1() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    public void constructorTest2() {
        User user = new User(UUID.randomUUID(), 1, "user", new Session());
        assertNotNull(user);
    }

    @Test
    public void constructorTest3() {
        User user = new User(UUID.randomUUID(), 1, "user", "ip", new Session());
        assertNotNull(user);
    }

    @Test
    public void setGetUserIdTest() {
        User user = new User();
        user.setId(1);
        assertEquals(1, user.getId());
    }

    @Test
    public void setGetTokenTest() {
        User user = new User();
        UUID token = UUID.randomUUID();
        user.setToken(token);
        assertEquals(token, user.getToken());
    }

    @Test
    public void setGetRoleTest() {
        User user = new User();
        int role = 1;
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    public void setGetNicknameTest() {
        User user = new User();
        String nickname = "user";
        user.setNickname(nickname);
        assertEquals(nickname, user.getNickname());
    }

    @Test
    public void equalsReflexiveTest() {
        User user1 = users.get(0);
        assertEquals(user1, user1);
    }

    @Test
    public void equalsSameTest() {
        User user1 = users.get(0);
        User user2 = users.get(1);
        assertEquals(user1, user2);
    }

    @Test
    public void equalsSymmetricTest() {
        User user1 = users.get(0);
        User user2 = users.get(1);
        boolean equals1 = user1.equals(user2);
        boolean equals2 = user2.equals(user1);
        assertEquals(true, equals1 == equals2);
    }

    @Test
    public void equalsDifferentIdTest() {
        User user1 = users.get(0);
        User user2 = users.get(1);
        long newId = 2;
        user2.setId(newId);
        assertNotEquals(user1, user2);
    }

    @Test
    public void equalsDifferentTokenTest() {
        User user1 = users.get(0);
        User user2 = users.get(1);
        UUID token = user2.getToken();
        UUID newToken = UUID.randomUUID();
        while (newToken.equals(token)) {
            newToken = UUID.randomUUID();
        }
        user2.setToken(newToken);
        assertNotEquals(user1, user2);
    }

    @Test
    public void equalsDifferentRoleTest() {
        User user1 = users.get(0);
        User user2 = users.get(1);
        int newRole = 2;
        user2.setRole(newRole);
        assertNotEquals(user1, user2);
    }

    @Test
    public void equalsDifferentNicknameTest() {
        User user1 = users.get(0);
        User user2 = users.get(1);
        String newNickname = "user2";
        user2.setNickname(newNickname);
        assertNotEquals(user1, user2);
    }

    @Test
    public void equalsDifferentIpAddressTest() {
        User user1 = users.get(0);
        User user2 = users.get(1);
        String newIpAddress = "ip2";
        user2.setIpAddress(newIpAddress);
        assertNotEquals(user1, user2);
    }

    @Test
    public void equalsDifferentSessionTest() {
        User user1 = users.get(0);
        User user2 = users.get(1);
        Session newSession = new Session();
        newSession.setId(2);
        user2.setSession(newSession);
        assertNotEquals(user1, user2);
    }

    @Test
    public void equalsTransitiveTest() {
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user4 = users.get(3);
        boolean u1EqualsU2 = user1.equals(user2);
        boolean u2EqualsU4 = user2.equals(user4);
        boolean u1EqualsU4Actual = user1.equals(user4);
        boolean u1EqualsU4Theoretical;
        if (u1EqualsU2 == u2EqualsU4) {
            u1EqualsU4Theoretical = true;
        } else {
            u1EqualsU4Theoretical = false;
        }
        assertEquals(u1EqualsU4Actual, u1EqualsU4Theoretical);
    }

    @Test
    public void equalsConsistentTest() {
        User user1 = users.get(0);
        User user5 = users.get(4);
        boolean equals1 = user1.equals(user5);
        user5.setId(2);
        boolean equals2 = user1.equals(user5);

        assertNotEquals(equals1, equals2);
    }

    @Test
    public void equalsNullTest() {
        User user1 = users.get(0);
        User user5 = null;
        assertEquals(false, user1.equals(user5));
    }

    @Test
    public void hashCodeEqualsConsistentTest() {
        User user1 = users.get(0);
        User user5 = users.get(4);
        boolean equalsHashCode = user1.hashCode() == user5.hashCode();

        assertEquals(true, equalsHashCode);
    }
}

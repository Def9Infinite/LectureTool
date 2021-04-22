package nl.tudelft.oopp.project;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class UserTest {
    UUID uuid = UUID.randomUUID();
    Session session = new Session(1,uuid,uuid,"sess");

    @Test
    public void noIdConstructorTest() {
        User user = new User(UUID.randomUUID(),1,"Mirko",session);
        Assertions.assertEquals(0,user.getId());
    }

    @Test
    public void equalsSame() {
        User user = new User(UUID.randomUUID(),1,"Mirko",session);
        User user1 = user;
        Assertions.assertTrue(user.equals(user));
    }

    @Test
    public void equalsNull() {
        User user = new User(UUID.randomUUID(),1,"Mirko",session);
        User user1 = null;
        Assertions.assertTrue(user.equals(user));
    }

    @Test
    public void hashTest() {
        User user = new User(uuid,1,"Mirko",session);
        User user1 = new User(uuid,1,"Mirko",session);
        Assertions.assertTrue(user.hashCode() == user1.hashCode());
    }
}

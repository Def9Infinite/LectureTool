package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

import nl.tudelft.oopp.project.controllers.UserController;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.User;
import nl.tudelft.oopp.project.exceptions.SessionClosedException;
import nl.tudelft.oopp.project.repository.EventRepository;
import nl.tudelft.oopp.project.repository.SessionRepository;
import nl.tudelft.oopp.project.repository.UserRepository;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;




@DataJpaTest
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EventRepository events;

    @Before
    public void clearDatabase() {
        userRepository.deleteAll();
    }

    @Test
    public void saveandretrieveTest() {
        Session session = new Session(UUID.randomUUID(),UUID.randomUUID(),null);
        User u = new User(UUID.randomUUID(),1,"test",session);
        //UUID's are server generated when saved to repository
        User user = userRepository.save(u);
        User user2 = userRepository.getOne(user.getId());
        assertEquals(user,user2);
    }

    @Test
    public void postUserTest() throws ParseException {
        Session session = new Session(UUID.randomUUID(),UUID.randomUUID(),null);
        session = sessionRepository.save(session);
        User u = new User("NickName");
        UserController userController = new UserController(userRepository, sessionRepository, events);
        User user = userController.createUser(session.getStudentToken(), u);
        assertEquals(u.getNickname(),user.getNickname());
    }

    @Test
    public void postUserInvalidSessionTokenTest() {
        Session session = new Session(UUID.randomUUID(),UUID.randomUUID(),null);
        session = sessionRepository.save(session);
        User u = new User("NickName");
        UserController userController = new UserController(userRepository, sessionRepository, events);
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> {
            userController.createUser(UUID.randomUUID(), u);
        });
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void postUserClosedSessionTest() {
        Session session = new Session(UUID.randomUUID(), UUID.randomUUID(), null);
        session.setClosed(true);
        session = sessionRepository.save(session);
        UUID token = session.getStudentToken();
        User u = new User("NickName");
        UserController userController = new UserController(userRepository, sessionRepository, events);
        SessionClosedException e = assertThrows(SessionClosedException.class, () -> {
            userController.createUser(token, u);
        });
    }

    @Test
    public void setRequestTest() {
        HttpServletRequest request = new MockHttpServletRequest();
        UserController userController = new UserController(userRepository, sessionRepository, events);
        userController.setRequest(request);
        assertEquals(request,userController.getRequest());
    }
}

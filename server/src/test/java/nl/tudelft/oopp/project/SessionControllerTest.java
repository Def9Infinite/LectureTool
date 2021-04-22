package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.project.controllers.SessionController;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.User;
import nl.tudelft.oopp.project.repository.SessionRepository;

import nl.tudelft.oopp.project.repository.SpeedFeedbackRepository;
import nl.tudelft.oopp.project.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.web.server.ResponseStatusException;


@DataJpaTest
public class SessionControllerTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpeedFeedbackRepository speedFeedbackRepository;

    @Before
    public void clearDatabase() {
        sessionRepository.deleteAll();
    }


    @Test
    public void saveandretrieveTest() {
        UUID uuid = UUID.randomUUID();
        Session session = new Session(uuid,uuid,null);
        //UUID's are server generated when saved to repository
        Session session1 = sessionRepository.save(session);
        Session session2 = sessionRepository.getOne(session1.getId());
        assertEquals(session1,session2);
    }

    @Test
    public void createSessionTest() {
        SessionController sessionController = new SessionController(sessionRepository);
        assertNotNull(sessionController.createSession(new Session()));
    }

    @Test
    public void testGetSessionToken() {
        SessionController sessionController = new SessionController(sessionRepository);
        UUID uuid = UUID.randomUUID();
        Session session = new Session(uuid,uuid,null);
        Session session1 = sessionRepository.save(session);
        Session session2 = sessionController.getSession(session1.getStudentToken());
        assertEquals(session2,session1);
    }

    @Test public void testUnkownSessionToken() {
        SessionController sessionController = new SessionController(sessionRepository);
        assertThrows(ResponseStatusException.class, () -> {
            sessionController.getSession(UUID.randomUUID());
        });
    }

    @Test
    public void modToken() {
        SessionController sessionController = new SessionController(sessionRepository);
        UUID student = UUID.randomUUID();
        UUID mod = UUID.randomUUID();
        Session session = new Session(mod,student,"test");
        Session session1 = sessionRepository.save(session);
        Session session2 = sessionController.getSession(session1.getPowerToken());
        assertEquals(session1,session2);
    }

    @Test
    public void unknownTokenTest() {
        SessionController sessionController = new SessionController(sessionRepository);
        UUID student = UUID.randomUUID();
        UUID mod = UUID.randomUUID();
        Session session = new Session(mod,student,"test");
        Session session1 = sessionRepository.save(session);
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            sessionController.getSession(UUID.randomUUID());
        });
        assertNotNull(exception);
    }

    @Test
    public void getSpeedFeedback5ItemsNoneNullTest() {
        SessionController sessionController = new SessionController(sessionRepository, speedFeedbackRepository);
        Session session = new Session(UUID.randomUUID(),UUID.randomUUID(),"test");
        sessionRepository.save(session);
        User user = new User(UUID.randomUUID(), 1, "NickName", session);
        userRepository.save(user);
        List<Integer> result = sessionController.getSpeedFeedback(user);
        assertEquals(5, result.size());
        assertFalse(result.contains(null));
    }

    @Test
    public void updateSpeedFeedbackInvalidSpeedTest() {
        SessionController sessionController = new SessionController(sessionRepository, speedFeedbackRepository);
        Session session = new Session(UUID.randomUUID(),UUID.randomUUID(),"test");
        sessionRepository.save(session);
        User user = new User(UUID.randomUUID(), 1, "NickName", session);
        userRepository.save(user);
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            sessionController.updateSpeedFeedback(user, 6);
        });
    }

}

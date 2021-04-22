package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.UUID;

import nl.tudelft.oopp.project.controllers.EventController;
import nl.tudelft.oopp.project.controllers.QuestionController;
import nl.tudelft.oopp.project.entities.Event;
import nl.tudelft.oopp.project.entities.Question;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.User;
import nl.tudelft.oopp.project.repository.EventRepository;
import nl.tudelft.oopp.project.repository.QuestionRepository;
import nl.tudelft.oopp.project.repository.SessionRepository;
import nl.tudelft.oopp.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class EventControllerTest {

    Session session = new Session();
    User user = new User(UUID.randomUUID(), 1, "NickName", session);
    Question quest = new Question("Test",user,session);
    Timestamp time = new Timestamp(System.currentTimeMillis());


    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @Test
    public void saveAndRetrieveTest() {
        EventController eventController = new EventController(userRepository,
                sessionRepository,questionRepository,eventRepository);
        Event event = new Event(session,time, "Created User");
        event = eventRepository.save(event);
        assertEquals(event,eventRepository.getOne(event.getId()));
    }

    @Test
    public void toLogTest() {

        sessionRepository.save(session);
        Event event = new Event(session,time, "Created User");
        Event event2 = new Event(session,time, "Deleted User");
        Event event3 = new Event(session,time, "Created Question");
        eventRepository.save(event);
        eventRepository.save(event2);
        eventRepository.save(event3);
        EventController eventController = new EventController(userRepository,
                sessionRepository,questionRepository,eventRepository);
        assertEquals("Created User on: " + time + "\n"
                + "Deleted User on: " + time + "\n"
                + "Created Question on: " + time + "\n",eventController.getLogs(user));
    }
}

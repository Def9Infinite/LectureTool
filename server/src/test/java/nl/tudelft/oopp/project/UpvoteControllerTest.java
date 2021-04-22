package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import nl.tudelft.oopp.project.controllers.UpvoteController;
import nl.tudelft.oopp.project.entities.Question;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.Upvote;
import nl.tudelft.oopp.project.entities.User;
import nl.tudelft.oopp.project.repository.EventRepository;
import nl.tudelft.oopp.project.repository.QuestionRepository;
import nl.tudelft.oopp.project.repository.SessionRepository;
import nl.tudelft.oopp.project.repository.UpvoteRepository;
import nl.tudelft.oopp.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UpvoteControllerTest {
    Session session = new Session(UUID.randomUUID(), UUID.randomUUID(), "test");
    User user = new User(UUID.randomUUID(), 0, "nick", session);
    Question question = new Question("text", user, session);
    Upvote upvote;

    @Autowired
    UpvoteRepository upvoteRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    /**
     * Clears upvote repository and instantiates needed objects.
     */
    @BeforeEach
    public void clearDatabase() {
        upvoteRepository.deleteAll();

        session = new Session();
        user = new User(UUID.randomUUID(), 0, "user", session);
        question = new Question("question", user, session);
        session.setSessionName("sessionName");
        session.setStudentToken(user.getToken());
        upvote = new Upvote(question, user);
    }

    @Test
    public void saveAndRetrieveTest() {
        userRepository.save(user);
        sessionRepository.save(session);
        questionRepository.save(question);
        Upvote savedUpvote = upvoteRepository.save(upvote);
        Upvote retrievedUpvote = upvoteRepository.getOne(upvote.getId());
        assertEquals(savedUpvote, retrievedUpvote);
    }

    @Test
    public void postUpvoteTest() {
        userRepository.save(user);
        sessionRepository.save(session);
        questionRepository.save(question);
        UpvoteController upvoteController = new UpvoteController(upvoteRepository,
                sessionRepository, questionRepository, eventRepository);
        Upvote createdUpvote = upvoteController.createUpvote(question.getId(), user);
        assertEquals(createdUpvote.getUser(), user);
    }

    @Test
    public void deleteUpvoteTest1() {
        userRepository.save(user);
        sessionRepository.save(session);
        questionRepository.save(question);
        UpvoteController upvoteController = new UpvoteController(upvoteRepository,
                sessionRepository, questionRepository, eventRepository);
        Upvote createdUpvote = upvoteController.createUpvote(question.getId(), user);
        Upvote deletedUpvote = upvoteController.deleteUpvote(question.getId(), user);
        assertEquals(0, upvoteRepository.count());
    }
}

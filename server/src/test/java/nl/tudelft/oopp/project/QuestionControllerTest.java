package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import nl.tudelft.oopp.project.controllers.QuestionController;
import nl.tudelft.oopp.project.entities.Question;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.Upvote;
import nl.tudelft.oopp.project.entities.User;
import nl.tudelft.oopp.project.exceptions.SessionClosedException;
import nl.tudelft.oopp.project.repository.EventRepository;
import nl.tudelft.oopp.project.repository.QuestionRepository;

import nl.tudelft.oopp.project.repository.SessionRepository;
import nl.tudelft.oopp.project.repository.UpvoteRepository;
import nl.tudelft.oopp.project.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.web.server.ResponseStatusException;




@DataJpaTest
public class QuestionControllerTest {

    Session session = new Session();
    User user = new User(UUID.randomUUID(), 1, "NickName", session);
    User user2 = new User(UUID.randomUUID(), 0, "NickName", session);
    Question quest = new Question("Test",user,session);


    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;
    
    @Autowired
    UpvoteRepository upvoteRepository;


    @Before
    public void clearDatabase() {
        questionRepository.deleteAll();
        user.setIpAddress("192:000");
    }

    @Test
    public void saveAndRetrieveTest() {
        QuestionController questionController = new QuestionController(questionRepository,
                upvoteRepository, eventRepository);
        Question question = questionRepository.save(quest);
        Question question1 = questionRepository.getOne(question.getId());
        assertEquals(question,question1);
    }

    @Test
    public void postQuestionTest() {
        QuestionController questionController = new QuestionController(questionRepository,
                upvoteRepository,eventRepository);
        Question question = questionController.createQuestion(quest, user);
        assertEquals(quest.getText(),question.getText());
    }

    @Test
    public void getQuestionsTest() {
        sessionRepository.save(session);
        userRepository.save(user);
        QuestionController questionController = new QuestionController(questionRepository,
                upvoteRepository, eventRepository);
        Question question = questionRepository.save(quest);
        Question question1 = new Question("Question?", user, session);
        questionRepository.save(question1);
        List<Question> questions = questionController.getQuestions(user);
        assertEquals(2,questions.size());
    }

    @Test
    public void getQuestionsTestId() {
        sessionRepository.save(session);
        userRepository.save(user);
        User user2 = new User(UUID.randomUUID(), 1, "NickName", session);
        user2 = userRepository.save(user2);
        Question question2 = new Question("Question?",user2,session);
        QuestionController questionController = new QuestionController(questionRepository,
                upvoteRepository,eventRepository);
        Question question = questionRepository.save(quest);
        Question question1 = new Question("Question?", user, session);
        questionRepository.save(question1);
        questionRepository.save(question2);
        List<Question> questions = questionController.getQuestionsUser(user2);
        assertEquals(1,questions.size());
    }

    @Test
    public void editQuestionsTest() {
        QuestionController questionController = new QuestionController(questionRepository,
                upvoteRepository, eventRepository);
        Question question = questionRepository.save(quest);
        Question update = questionController
                .editQuestion(question.getSession().getId(),question.getId(),"Updated", user);
        //check if the object with the old id now contains the correct text.
        assertEquals(questionRepository.findById(question.getId()).orElse(null).getText(),
                update.getText());
    }

    @Test
    public void deleteQuestionTest() {
        sessionRepository.save(session);
        userRepository.save(user);
        QuestionController questionController = new QuestionController(questionRepository,
                upvoteRepository, eventRepository);
        Question question = questionRepository.save(quest);
        Question question1 = new Question("Question?", user, session);
        questionRepository.save(question1);
        questionController.deleteQuestions(user,question.getId());
        assertEquals(1,questionRepository.count());
    }

    @Test
    public void deleteQuestionTestWrongUser() {
        sessionRepository.save(session);
        userRepository.save(user);
        User user1 = new User();
        QuestionController questionController = new QuestionController(questionRepository);
        Question question = questionRepository.save(quest);
        assertNull(questionController.deleteQuestions(user1,question.getId()));
    }

    @Test
    public void updateQuestionNoMod() {
        sessionRepository.save(session);
        userRepository.save(user2);
        questionRepository.save(quest);
        QuestionController questionController = new QuestionController(questionRepository);
        assertThrows(ResponseStatusException.class, () -> {
            questionController.updateQuestion(quest, user2);
        });
    }

    @Test
    public void updateQuestionTest() {
        sessionRepository.save(session);
        userRepository.save(user);
        questionRepository.save(quest);
        QuestionController questionController = new QuestionController(questionRepository, eventRepository);
        quest.setAnswer("test123");
        Question returnQuest = questionController.updateQuestion(quest, user);
        assertTrue(returnQuest.getAnswerStatus());
        assertEquals("test123", returnQuest.getAnswer());
    }

    @Test
    public void emptyConstructor() {
        assertNotNull(new QuestionController());
    }

    @Test
    public void constructor2() {
        assertNotNull(new QuestionController(questionRepository, upvoteRepository));
    }

    @Test
    public void getSessionNull()  {
        Session session = new Session(UUID.randomUUID(),UUID.randomUUID(),"Test");
        session.setClosed(true);
        sessionRepository.save(session);
        user.setSession(session);
        userRepository.save(user);
        QuestionController questionController = new QuestionController(questionRepository, eventRepository);
        SessionClosedException thrown = Assertions.assertThrows(SessionClosedException.class,
            () -> questionController.createQuestion(quest,user));
        Assertions.assertNotNull(thrown);
    }

    @Test
    public void getAnswered() {
        sessionRepository.save(session);
        userRepository.save(user);
        questionRepository.save(quest);
        QuestionController questionController = new QuestionController(questionRepository, eventRepository);
        Assertions.assertEquals(0,questionController.getAnsweredQuestions(user).size());
    }

    @Test
    public void markAnswered() {
        sessionRepository.save(session);
        userRepository.save(user);
        questionRepository.save(quest);
        QuestionController questionController = new QuestionController(questionRepository, eventRepository);
        questionController.markAnswerQuestions(user,quest.getId(),true);
        Assertions.assertTrue(questionRepository.getOne(quest.getId()).getAnswerStatus());
    }

    @Test
    public void markAnsweredOwn() {
        Set<Upvote> set = new HashSet<>();
        quest.setUpvotes(set);
        questionRepository.save(quest);
        sessionRepository.save(session);
        user.setRole(0);
        userRepository.save(user);
        QuestionController questionController = new QuestionController(questionRepository, eventRepository);
        questionController.markAnswerQuestions(user,quest.getId(),true);
        Assertions.assertTrue(questionRepository.getOne(quest.getId()).getAnswerStatus());
    }

    @Test
    public void export() {
        Set<Upvote> set = new HashSet<>();
        quest.setUpvotes(set);
        questionRepository.save(quest);
        sessionRepository.save(session);
        userRepository.save(user);
        QuestionController questionController = new QuestionController(questionRepository, eventRepository);
        Assertions.assertEquals("0,Test,false,NickName,,"
                        + quest.getCreatedAt().getHour() + ":" + quest.getCreatedAt().getMinute()
                        + System.getProperty("line.separator"), questionController.exportQuestions(user));
    }

    @Test
    public void exportStudent() {
        sessionRepository.save(session);
        userRepository.save(user);
        QuestionController questionController = new QuestionController(questionRepository, eventRepository);
        Assertions.assertEquals("",questionController.exportQuestions(user));
    }

}

package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nl.tudelft.oopp.project.controllers.PollController;
import nl.tudelft.oopp.project.entities.Poll;
import nl.tudelft.oopp.project.entities.PollOption;
import nl.tudelft.oopp.project.entities.PollResponse;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.User;
import nl.tudelft.oopp.project.repository.PollOptionRepository;
import nl.tudelft.oopp.project.repository.PollRepository;
import nl.tudelft.oopp.project.repository.PollResponseRepository;
import nl.tudelft.oopp.project.repository.SessionRepository;
import nl.tudelft.oopp.project.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class PollControllerTest {

    Session session = new Session();
    User user = new User(UUID.randomUUID(), 1, "NickName", session);
    Poll poll = new Poll();
    PollOption option = new PollOption("A",poll);
    PollResponse response = new PollResponse();


    @Autowired
    PollRepository polls;

    @Autowired
    PollOptionRepository options;

    @Autowired
    PollResponseRepository responses;

    @Autowired
    UserRepository users;

    @Autowired
    SessionRepository sessions;

    Session session1 = new Session();

    @Before
    public void saveEntities() {
        users.save(user);
        session1 = sessions.save(session);
    }

    @Test
    public void saveAndRetrieveTest() {
        Poll poll = new Poll();
        PollOption option = new PollOption("A",poll);
        PollResponse response = new PollResponse();
        Poll poll1 = polls.save(poll);
        PollOption option1 = options.save(option);
        PollResponse response1 = responses.save(response);
        Assertions.assertEquals(poll1,polls.findById(poll1.getId()).get());
        Assertions.assertEquals(option1,options.getOne(option1.getId()));
        Assertions.assertEquals(response1,responses.getOne(response1.getId()));
    }

    @Test
    public void createPoll() {
        sessions.save(session);
        users.save(user);
        PollController pollController = new PollController(polls,options,responses,users,sessions);
        poll.setSession(session1);
        pollController.createPoll(user,poll);
        assertEquals(1,polls.count());
    }

    @Test
    public void getPolls() {
        sessions.save(session);
        users.save(user);
        List<Poll> pollList = new ArrayList<>();
        pollList.add(polls.save(poll));
        PollController pollController = new PollController(polls,options,responses,users,sessions);
        assertEquals(0,pollController.getPolls(user).size());
    }

    @Test
    public void toggle() {
        PollController pollController = new PollController(polls,options,responses,users,sessions);
        sessions.save(session);
        users.save(user);
        Poll poll1 = polls.save(poll);
        pollController.togglePoll(poll1.getId(),true,user);
        assertEquals(true, polls.getOne(poll1.getId()).getOpen());
    }

    @Test
    public void vote() {
        sessions.save(session);
        users.save(user);
        options.save(option);
        Poll poll1 = polls.save(poll);
        PollController pollController = new PollController(polls,options,responses,users,sessions);
        pollController.setVote(poll1.getId(),option.getId(),user);
        assertEquals(1,responses.count());
    }
}

package nl.tudelft.oopp.project.controllers;

import java.util.List;
import java.util.stream.Collectors;

import nl.tudelft.oopp.project.PollingEventsController;
import nl.tudelft.oopp.project.annotations.RequireUserContext;
import nl.tudelft.oopp.project.entities.Poll;
import nl.tudelft.oopp.project.entities.PollDto;
import nl.tudelft.oopp.project.entities.PollOption;
import nl.tudelft.oopp.project.entities.PollResponse;
import nl.tudelft.oopp.project.entities.User;
import nl.tudelft.oopp.project.repository.PollOptionRepository;
import nl.tudelft.oopp.project.repository.PollRepository;
import nl.tudelft.oopp.project.repository.PollResponseRepository;
import nl.tudelft.oopp.project.repository.SessionRepository;
import nl.tudelft.oopp.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Autowired
    private PollResponseRepository pollResponseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    /**
     * Constructor for testing purposes.
     * @param pollRepository polls
     * @param pollOptionRepository options
     * @param pollResponseRepository response
     * @param userRepository users
     * @param sessionRepository sessions
     */
    public PollController(PollRepository pollRepository, PollOptionRepository pollOptionRepository,
            PollResponseRepository pollResponseRepository, UserRepository userRepository,
            SessionRepository sessionRepository) {
        this.pollRepository = pollRepository;
        this.pollOptionRepository = pollOptionRepository;
        this.pollResponseRepository = pollResponseRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Creates poll.
     * @param userContext authenticated user.
     * @param poll poll.
     */
    @PostMapping("/poll")
    @ResponseBody
    @RequireUserContext
    public void createPoll(@RequestAttribute("user_context") User userContext,
                           @RequestBody Poll poll) {
        if (userContext.getRole() != 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        poll.setUser(userContext);
        poll.setSession(userContext.getSession());
        poll.setOpen(true);
        pollRepository.save(poll);
        PollingEventsController.emit(userContext.getSession(), "refresh_polls");
    }

    /**
     * Returns a list of polls for the user's session order by id.
     * @param userContext authenticated user
     * @return list of polls
     */
    @GetMapping("/poll")
    @ResponseBody
    @RequireUserContext
    public List<PollDto> getPolls(@RequestAttribute("user_context") User userContext) {
        List<Poll> dbList = pollRepository.findAllBySessionOrderByIdDesc(userContext.getSession());
        List<PollDto> returnList = dbList.stream().map(PollDto::new).collect(Collectors.toList());
        return returnList;
    }

    /**
     * Toggle poll closed or open.
     * @param pollId poll to toggle
     * @param userContext authenticated user.
     */
    @PostMapping("/poll/{poll_id}/toggle")
    @ResponseBody
    @RequireUserContext
    public void togglePoll(@PathVariable(name = "poll_id") long pollId,
                        @RequestBody boolean action,
                        @RequestAttribute("user_context") User userContext) {
        if (userContext.getRole() != 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Poll poll = pollRepository.findById(pollId).get();
        poll.setOpen(action);
        pollRepository.save(poll);
        PollingEventsController.emit(userContext.getSession(), "refresh_polls");
    }

    /**
     * Vote for a polloption.
     * @param pollId poll to vote on
     * @param pollOptionId chosen option
     * @param userContext authenticated user
     */
    @PutMapping("/poll/{poll_id}")
    @ResponseBody
    @RequireUserContext
    public void setVote(@PathVariable(name = "poll_id") long pollId,
                        @RequestBody long pollOptionId,
                        @RequestAttribute("user_context") User userContext) {
        PollOption pollOption = pollOptionRepository.findById(pollOptionId).get();
        PollResponse currentResponse = pollResponseRepository
                .getPollResponseByUserAndPollOptionPoll(userContext,pollOption.getPoll());
        if (currentResponse != null) {
            pollResponseRepository.delete(currentResponse);
        }
        PollResponse newResponse = new PollResponse(userContext, pollOption);
        pollResponseRepository.save(newResponse);
        PollingEventsController.emit(userContext.getSession(), "refresh_polls");
    }

}

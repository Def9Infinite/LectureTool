package nl.tudelft.oopp.project.controllers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import nl.tudelft.oopp.project.PollingEventsController;
import nl.tudelft.oopp.project.annotations.RequireUserContext;
import nl.tudelft.oopp.project.entities.Event;
import nl.tudelft.oopp.project.entities.Question;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.User;
import nl.tudelft.oopp.project.exceptions.IpBannedException;
import nl.tudelft.oopp.project.exceptions.SessionClosedException;
import nl.tudelft.oopp.project.repository.EventRepository;
import nl.tudelft.oopp.project.repository.QuestionRepository;
import nl.tudelft.oopp.project.repository.SessionRepository;
import nl.tudelft.oopp.project.repository.UpvoteRepository;
import nl.tudelft.oopp.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class UserController {

    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EventRepository events;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UpvoteRepository upvoteRepository;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Constructor with 3 repositories.
     * @param userRepository user repo
     * @param sessionRepository session repo
     * @param events event repo
     */
    public UserController(UserRepository userRepository, SessionRepository sessionRepository, EventRepository events) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.events = events;
    }

    /**
     * Post endpoint to create a User.
     *
     * @return created user.
     */
    @PostMapping("/session/{token}/user")
    @ResponseBody
    public User createUser(@PathVariable(name = "token") UUID sessionToken, @RequestBody User u) throws ParseException {
        // Real projects should support X-FORWARDED-FOR header if hosted behind proxy.
        if (request != null) {
            u.setIpAddress(request.getRemoteAddr());
        }
        Session session = sessionRepository.findByAnyToken(sessionToken);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No session with this token found.");
        } else if (session.getStudentToken().equals(sessionToken) && session.getClosed()) {
            throw new SessionClosedException(session);
        } else if (session.ipIsBanned(u.getIpAddress())) {
            throw new IpBannedException(session);
        }
        if (session.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss.SSS");
            Date sessDate = sdf.parse(session.getDate());
            Date currDate = new Date(System.currentTimeMillis());
            if (sessDate.compareTo(currDate) > 0) {
                throw new ResponseStatusException(HttpStatus.LOCKED,
                        "Session doesn't start until: " + session.getDate());
            }
        }
        u.setSession(session);
        u.setToken(UUID.randomUUID());

        if (sessionToken.equals(session.getPowerToken())) {
            u.setRole(1);
        } else {
            u.setRole(0);
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Event event = new Event(u.getSession(),timestamp, "Created User: "
                + u.getNickname() + " IpAddress: " + u.getIpAddress());
        User user = userRepository.save(u);
        events.save(event);
        return user;
    }

    /**
     * Get endpoint for getting an User.
     *
     * @param usertoken user id of the User
     * @return the User object
     */
    @GetMapping("/session/{token}/user/{usertoken}")
    @ResponseBody
    public User findUser(@PathVariable(name = "token") UUID sessionToken,
                         @PathVariable(name = "usertoken") UUID usertoken) {
        Session session = sessionRepository.findByAnyToken(sessionToken);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No session with this token found.");
        } else {
            if (userRepository.findByToken(usertoken) != null) {
                return userRepository.findByToken(usertoken);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with this token found.");
            }
        }
    }

    /**
     * Bans a user and deletes all questions asked by that user.
     * @param userContext logged in user.
     * @param u User to be banned.
     */
    @PostMapping("/user/ban")
    @ResponseBody
    @RequireUserContext
    @Transactional
    public void banUser(@RequestAttribute("user_context") User userContext, @RequestBody User u) {
        if (userContext.getRole() != 1) { // Not a mod
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Session session = userContext.getSession();
        User userToBeBanned = userRepository.getOne(u.getId());
        session.banIp(userToBeBanned.getIpAddress());
        sessionRepository.save(session);
        for (Question q : questionRepository.findAllBySessionIdAndUserId(session.getId(), u.getId())) {
            upvoteRepository.removeAllByQuestion(q);
        }
        questionRepository.deleteAllByUser(userToBeBanned);
        PollingEventsController.emit(userContext.getSession(), "refresh_questions");
    }

    //testing purposes
    public HttpServletRequest getRequest() {
        return request;
    }
}

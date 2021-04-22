package nl.tudelft.oopp.project.controllers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.project.PollingEventsController;
import nl.tudelft.oopp.project.annotations.RequireUserContext;
import nl.tudelft.oopp.project.entities.Event;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.SpeedFeedback;
import nl.tudelft.oopp.project.entities.User;
import nl.tudelft.oopp.project.repository.EventRepository;
import nl.tudelft.oopp.project.repository.SessionRepository;

import nl.tudelft.oopp.project.repository.SpeedFeedbackRepository;
import nl.tudelft.oopp.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class SessionController {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpeedFeedbackRepository speedFeedbackRepository;

    @Autowired
    private EventRepository events;

    public SessionController() {

    }

    public SessionController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public SessionController(SessionRepository sessionRepository, SpeedFeedbackRepository speedFeedbackRepository) {
        this.sessionRepository = sessionRepository;
        this.speedFeedbackRepository = speedFeedbackRepository;
    }

    /**
     * Post endpoint for session creation.
     *
     * @return created session.
     */
    @PostMapping("session")
    @ResponseBody
    public Session createSession(@RequestBody Session session) {
        return sessionRepository.save(session);
    }

    /**
     * Get Endpoint to return session using a token.
     *
     * @param token Student or power (mod/lecturer) token which identifies a session.
     * @return session which matches
     */
    @GetMapping("session/{token}")
    @ResponseBody
    public Session getSession(@PathVariable(name = "token") UUID token) {
        Session session = sessionRepository.findByAnyToken(token);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown session token.");
        } else {
            return session;
        }
    }

    /**
     * Get session by user token.
     * @param id user token
     * @return Session that the user is in
     */
    @GetMapping("user/{token}/session")
    @ResponseBody
    public Session getSessionByUserToken(@PathVariable(name = "token") String id) throws ParseException {
        if (userRepository.findByToken(UUID.fromString(id)) != null) {
            return userRepository.findByToken(UUID.fromString(id)).getSession();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown user token.");
        }

    }

    /**
     * Marks current session as closed.
     * @param userContext authenticated user
     */
    @PostMapping("session/close")
    @ResponseBody
    @RequireUserContext
    public void closeSession(@RequestAttribute("user_context") User userContext) {
        if (userContext.getRole() != 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Session s = userContext.getSession();
        s.setClosed(true);
        sessionRepository.save(s);
    }

    /**
     * Returns list of 5 items, containing the speed votes from slow to fast.
     * @param userContext current client user
     * @return list of 5 items, containing the speed votes from slow to fast
     */
    @GetMapping("session/speed")
    @ResponseBody
    @RequireUserContext
    public List<Integer> getSpeedFeedback(@RequestAttribute("user_context") User userContext) {
        List<Integer> speeds = speedFeedbackRepository.listLectureSpeedsForSession(userContext.getSession().getId());
        speeds.replaceAll(s -> s == null ? 0 : s);
        return speeds;
    }

    /**
     * Adds or sets the speedfeedback for a specific user.
     * @param userContext user who sent the request
     * @param feedbackValue speed value!
     */
    @PostMapping("session/speed")
    @ResponseBody
    @RequireUserContext
    public void updateSpeedFeedback(@RequestAttribute("user_context") User userContext,
                                    @RequestBody int feedbackValue) {
        if (feedbackValue < 0 || feedbackValue > 4) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Speed value has to be between 0 and 5.");
        }
        Session session = userContext.getSession();
        SpeedFeedback speedFeedback = speedFeedbackRepository.getByUserAndSession(session, userContext);
        if (speedFeedback != null) {
            speedFeedback.setSpeedFeedback(feedbackValue);
        } else {
            speedFeedback = new SpeedFeedback(session, userContext, feedbackValue);
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Event event = new Event(userContext.getSession(),timestamp,
                "Updated lecture speed feedback to: " + feedbackValue + " User: "
                        + userContext.getNickname() + " IpAddress: " + userContext.getIpAddress());
        events.save(event);
        PollingEventsController.emit(userContext.getSession(), "refresh_speed_feedback");
        speedFeedbackRepository.save(speedFeedback);
    }

    /**
     * Handles long-polling requests and thems to the PollingEventsController.
     * @param userContext current user;
     * @return deferredResult. Response is set on polling event.
     */
    @GetMapping("session/poll")
    @ResponseBody
    @RequireUserContext
    public DeferredResult<ResponseEntity<String>> pollForSessionEvents(
            @RequestAttribute("user_context") User userContext) {
        DeferredResult<ResponseEntity<String>> output;
        output = new DeferredResult<>(25000L, ResponseEntity.ok("timeout"));
        PollingEventsController.addListener(userContext.getSession(), output);
        return output;
    }

    @GetMapping("session/{token}/time")
    @ResponseBody
    public String getStartTime(@PathVariable(name = "token") String token) {
        return sessionRepository.findByAnyToken(UUID.fromString(token)).getDate();
    }
}

package nl.tudelft.oopp.project.controllers;

import java.util.List;
import nl.tudelft.oopp.project.annotations.RequireUserContext;
import nl.tudelft.oopp.project.entities.Event;
import nl.tudelft.oopp.project.entities.User;
import nl.tudelft.oopp.project.repository.EventRepository;
import nl.tudelft.oopp.project.repository.QuestionRepository;
import nl.tudelft.oopp.project.repository.SessionRepository;
import nl.tudelft.oopp.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
public class EventController {

    @Autowired
    private UserRepository users;
    @Autowired
    private SessionRepository sessions;
    @Autowired
    private QuestionRepository questions;
    @Autowired
    private EventRepository events;

    /**
     * Controller with all repositories.
     * @param users user repository
     * @param sessions session repository
     * @param questions question repository
     * @param events event repository
     */
    public EventController(UserRepository users, SessionRepository sessions,
                           QuestionRepository questions, EventRepository events) {
        this.users = users;
        this.sessions = sessions;
        this.questions = questions;
        this.events = events;
    }

    /**
     * Add all string representations to one string and return it.
     * @param userContext user who send the request
     * @return string representation of the logs
     */
    @GetMapping("/event")
    @ResponseBody
    @RequireUserContext
    public String getLogs(@RequestAttribute("user_context") User userContext) {
        if (userContext.getRole() == 1) {
            String log = "";
            List<Event> eventList = events.findAllBySessionId(userContext.getSession().getId());
            for (int i = 0; i < eventList.size(); i++) {
                log += eventList.get(i).toLog();
            }
            return log;
        }
        return null;
    }
}

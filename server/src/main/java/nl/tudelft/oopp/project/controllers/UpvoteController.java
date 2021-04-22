package nl.tudelft.oopp.project.controllers;

import java.sql.Timestamp;
import java.util.List;

import nl.tudelft.oopp.project.PollingEventsController;
import nl.tudelft.oopp.project.annotations.RequireUserContext;
import nl.tudelft.oopp.project.entities.Event;
import nl.tudelft.oopp.project.entities.Question;
import nl.tudelft.oopp.project.entities.Upvote;
import nl.tudelft.oopp.project.entities.User;
import nl.tudelft.oopp.project.repository.EventRepository;
import nl.tudelft.oopp.project.repository.QuestionRepository;
import nl.tudelft.oopp.project.repository.SessionRepository;
import nl.tudelft.oopp.project.repository.UpvoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class UpvoteController {
    @Autowired
    UpvoteRepository upvotes;

    @Autowired
    SessionRepository sessions;

    @Autowired
    QuestionRepository questions;

    @Autowired
    EventRepository events;

    public UpvoteController() {
    }

    public UpvoteController(UpvoteRepository upvotes) {
        this.upvotes = upvotes;
    }

    /**
     * Constructor for Upvote. Creates a new Upvote.
     * @param upvotes - Upvote repository
     * @param sessions - Session repository
     * @param questions - Question repository
     */
    public UpvoteController(UpvoteRepository upvotes,
                            SessionRepository sessions,
                            QuestionRepository questions, EventRepository events) {
        this.upvotes = upvotes;
        this.sessions = sessions;
        this.questions = questions;
        this.events = events;
    }

    /**
     * Creates an upvote.
     * @param questionId - id of question for upvote to be created.
     * @param userContext - authenticated user
     * @return the created upvote
     */
    @RequireUserContext
    @PostMapping("/question/{question_id}/upvote")
    @ResponseBody
    public Upvote createUpvote(@PathVariable(name = "question_id") long questionId,
                               @RequestAttribute("user_context") User userContext) {
        Question question = questions.findById(questionId).orElse(null);
        Upvote upvote = new Upvote(question, userContext);
        Event event = new Event(userContext.getSession(),
                new Timestamp(System.currentTimeMillis()), "Upvoted Question: " + questionId
                + " User: " + userContext.getNickname() + " IpAddress: " + userContext.getIpAddress());
        events.save(event);
        PollingEventsController.emit(userContext.getSession(), "refresh_questions");
        return upvotes.save(upvote);
    }

    @RequireUserContext
    @GetMapping("/question/{id}/upvote/numUpvotes")
    @ResponseBody
    public int getNumUpvotes(@PathVariable(name = "id") long id,
                             @RequestAttribute("user_context") User userContext) {
        List<Upvote> upvotesList = upvotes.findAllByQuestionId(id);
        return upvotesList.size();
    }

    /**
     * Deletes an Upvote.
     * @param userContext - authorized user
     * @param questionId - id of Upvote to be deleted
     * @return the deleted Upvote
     */
    @RequireUserContext
    @DeleteMapping("/question/{question_id}/upvote")
    @ResponseBody
    public Upvote deleteUpvote(@PathVariable(name = "question_id") long questionId,
                               @RequestAttribute("user_context") User userContext) {
        Question question = questions.findById(questionId).orElse(null);
        Upvote upvote = upvotes.findUpvotesByUserAndQuestion(question, userContext);
        PollingEventsController.emit(userContext.getSession(), "refresh_questions");
        if (upvote != null) {
            Event event = new Event(userContext.getSession(),
                    new Timestamp(System.currentTimeMillis()), "Removed upvote from Question: " + questionId
                    + " User: " + userContext.getNickname() + " IpAddress: " + userContext.getIpAddress());
            events.save(event);
            upvotes.delete(upvote);
            return upvote;
        } else {
            return null;
        }
    }

}

package nl.tudelft.oopp.project.controllers;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javassist.expr.NewArray;
import nl.tudelft.oopp.project.PollingEventsController;
import nl.tudelft.oopp.project.annotations.RequireUserContext;
import nl.tudelft.oopp.project.entities.Event;
import nl.tudelft.oopp.project.entities.Question;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.Upvote;
import nl.tudelft.oopp.project.entities.User;

import nl.tudelft.oopp.project.exceptions.SessionClosedException;

import nl.tudelft.oopp.project.repository.EventRepository;
import nl.tudelft.oopp.project.repository.QuestionRepository;

import nl.tudelft.oopp.project.repository.UpvoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;


@Controller
public class QuestionController {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    EventRepository events;

    @Autowired
    UpvoteRepository upvoteRepository;

    public QuestionController() {
    }

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public QuestionController(QuestionRepository questionRepository, EventRepository eventRepository) {
        this.questionRepository = questionRepository;
        this.events = eventRepository;
    }

    public QuestionController(QuestionRepository questionRepository, UpvoteRepository upvoteRepository) {
        this.questionRepository = questionRepository;
        this.upvoteRepository = upvoteRepository;
    }

    /**
     * Constructor with 3 repositories.
     * @param questionRepository question repo
     * @param upvoteRepository upvote repo
     * @param events event repo
     */
    public QuestionController(QuestionRepository questionRepository, UpvoteRepository upvoteRepository,
                              EventRepository events) {
        this.questionRepository = questionRepository;
        this.upvoteRepository = upvoteRepository;
        this.events = events;
    }

    /**
     * Create question method.
     * @param question question to be created.
     * @param userContext authenticated user entity.
     * @return created question.
     */
    @RequireUserContext
    @PostMapping("/question")
    @ResponseBody
    public Question createQuestion(@RequestBody Question question,
                                   @RequestAttribute("user_context") User userContext) {
        Session session = userContext.getSession();
        if (session.getClosed()) {
            throw new SessionClosedException(session);
        }
        question.setUser(userContext);
        question.setSession(userContext.getSession());
        Question result = questionRepository.save(question);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Event event = new Event(userContext.getSession(),timestamp,"Created Question: " + result.getId()
            + " User: " + userContext.getNickname() + " IpAddress: " + userContext.getIpAddress());
        events.save(event);
        PollingEventsController.emit(userContext.getSession(), "refresh_questions");
        return result;
    }

    /**
     * Gets question from server, for the session of the user, ranked by relevance.
     * @param userContext authenticated user
     * @return questions
     */
    @RequireUserContext
    @GetMapping("/question")
    @ResponseBody
    public List<Question> getQuestions(@RequestAttribute("user_context") User userContext) {
        List<Question> questionList = questionRepository.getRankedQuestionsBySessionId(
                userContext.getSession().getId());
        return questionList;
    }

    /**
     * Returns ranked answered questions for the user's session.
     * @param userContext authenticated user
     * @return answered questions.
     */
    @RequireUserContext
    @GetMapping("/question/answered")
    @ResponseBody
    public List<Question> getAnsweredQuestions(@RequestAttribute("user_context") User userContext) {
        List<Question> questionList = questionRepository.getRankedAnsweredQuestionsBySessionId(
                userContext.getSession().getId());
        return questionList;
    }

    /**
     * Get all questions that the user who requested asked for.
     * @param userContext user who sent the request
     * @return list of all questions
     */
    @RequireUserContext
    @GetMapping("/question/{id}")
    @ResponseBody
    public List<Question> getQuestionsUser(@RequestAttribute("user_context") User userContext) {
        List<Question> questionList = questionRepository.findAllBySessionIdAndUserId(userContext
                .getSession().getId(), userContext.getId());
        return questionList;
    }

    /**
     * Edit question text.
     * @param sessionId id of the session it belongs to.
     * @param id id of the question to be edited
     * @param string the new text the question should have
     * @return the updated question object.
     */
    @RequireUserContext
    @PutMapping("/session/{sessionid}/question/{id}")
    @ResponseBody
    public Question editQuestion(@PathVariable(name = "sessionid") Long sessionId,
                                 @PathVariable(name = "id") Long id,
                                 @RequestBody String string,
                                 @RequestAttribute("user_context") User userContext) {
        Question question = questionRepository.findById(id).orElse(null);
        question.setText(string);
        PollingEventsController.emit(question.getSession(), "refresh_questions");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Event event = new Event(userContext.getSession(),timestamp,"Updated Question: "
                + question.getId() + " User: " + userContext.getNickname() + " IpAddress: "
                + userContext.getIpAddress());
        events.save(event);
        return questionRepository.save(question);
    }

    /**
     * Update question.
     * @param question question to be updated
     * @param userContext loggged in user
     * @return updated question
     */
    @PutMapping("/question")
    @ResponseBody
    @RequireUserContext
    public Question updateQuestion(@RequestBody Question question,
                                   @RequestAttribute("user_context") User userContext) {
        Question dbQuestion = questionRepository.findById(question.getId()).get();
        if (userContext.getRole() == 1 || dbQuestion.getUser().equals(userContext)) {
            dbQuestion.setAnswer(question.getAnswer());
            dbQuestion.setAnswerStatus(true);
            questionRepository.save(dbQuestion);
            PollingEventsController.emit(dbQuestion.getSession(), "refresh_questions");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Event event = new Event(userContext.getSession(),timestamp,"Updated Question: "
                    + question.getId() + " User: " + userContext.getNickname() + " IpAddress: "
                    + userContext.getIpAddress());
            events.save(event);
            return dbQuestion;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot edit this question.");
        }
    }


    /**
     * Delete the question with the specified id if moderator or own request.
     * @param userContext user who sent the request
     * @param id id of the question to be deleted
     * @return question that was deleted
     */
    @RequireUserContext
    @DeleteMapping("/question/{id}")
    @ResponseBody
    public Question deleteQuestions(@RequestAttribute("user_context") User userContext,
            @PathVariable(name = "id") long id) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question != null && question.getUser().equals(userContext) || userContext.getRole() == 1) {
            List<Upvote> upvotesToBeDeleted = upvoteRepository.findAllByQuestionId(id);
            if (upvotesToBeDeleted != null) {
                for (Upvote upvote : upvotesToBeDeleted) {
                    upvoteRepository.delete(upvote);
                }
            }
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Event event = new Event(userContext.getSession(), timestamp, "Deleted Question: "
                    + id + " User: " + userContext.getNickname() + " IpAddress: " + userContext.getIpAddress());
            events.save(event);
            questionRepository.delete(question);
            PollingEventsController.emit(userContext.getSession(), "refresh_questions");
            return question;
        } else {
            return null;
        }

    }

    /**
     * Mark question as answered as a moderator or mark own non-popular
     * question as answered as a student.
     * @param userContext user who sent the request
     * @param id id of the question to be marked as answered
     * @return question to be marked as answered
     */
    @RequireUserContext
    @PutMapping("/question/{id}")
    @ResponseBody
    public Question markAnswerQuestions(@RequestAttribute("user_context") User userContext,
                                        @PathVariable(name = "id") long id,
                                        @RequestBody boolean status) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question != null && userContext.getRole() == 1) {
            question.setAnswerStatus(status);
            Timestamp time = new Timestamp(System.currentTimeMillis());
            Event event = new Event(userContext.getSession(), time, "Set answer status to: "
                    + question.getAnswerStatus() + " for question: " + id + " User: " + userContext.getNickname()
                    + " IpAddress: " + userContext.getIpAddress());
            events.save(event);
            questionRepository.save(question);
            PollingEventsController.emit(userContext.getSession(), "refresh_questions");
            return question;
        } else if (question != null && (question.getUser().equals(userContext))) {
            if (question.getUpvotes().size() < 5) {
                question.setAnswerStatus(status);
                questionRepository.save(question);
                PollingEventsController.emit(userContext.getSession(), "refresh_questions");
                return question;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Create a string which contains csv from question + upvotes and return the string.
     * @param userContext user who send the request
     * @return string csv
     */
    @RequireUserContext
    @GetMapping("/question/export")
    @ResponseBody
    public String exportQuestions(@RequestAttribute ("user_context") User userContext) {
        if (userContext.getRole() == 1) {
            String result = "";
            List<Question> list = questionRepository.getRankedQuestionsBySessionId(
                    userContext.getSession().getId());
            list.addAll(questionRepository.getRankedAnsweredQuestionsBySessionId(userContext.getSession().getId()));
            for (Question question : list) {
                result += question.getUpvotes().size() + ","
                        + question.getText().replaceAll(",", "") + ","
                        + question.getAnswerStatus() + ","
                        + question.getUser().getNickname().replaceAll(",", "") + ","
                        + ((question.getAnswer() != null) ? question.getAnswer() : "").replaceAll(",", "") + ","
                        + question.getCreatedAt().getHour()
                        + ":" + question.getCreatedAt().getMinute() + System.getProperty("line.separator");
            }
            return result;
        }
        return null;
    }


}

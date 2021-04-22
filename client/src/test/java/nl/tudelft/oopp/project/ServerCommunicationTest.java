package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.google.gson.Gson;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import nl.tudelft.oopp.project.communication.ServerCommunication;

import nl.tudelft.oopp.project.exceptions.ServerResponseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Parameter;

public class ServerCommunicationTest {

    private static ClientAndServer mockserver;
    private static UUID uuid = UUID.randomUUID();
    private static Session session = new Session(0, uuid,uuid,"test");
    private static Session session2 = new Session("test","2021-12-11");
    private static User u = new User(0,uuid,1,"test",session);
    private static Question question = new Question(1,"text","",false, u);
    private static Question question1 = new Question(2,"MyQuestion","",false, u);
    private static Question question2 = new Question(3,"something","",true,u);
    private static List<Question> questions = new ArrayList<Question>();
    private static List<Question> answeredQuestions = new ArrayList<>();
    ServerCommunication serverCommunication = new ServerCommunication(HttpClient.newBuilder().build());
    private static Gson gson = new Gson();
    private static String uuidRegexPattern = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})";
    private static Upvote upvote = new Upvote(question, u);
    private static List<String> strings = new ArrayList<>();
    private static Poll poll = new Poll("Test", strings);


    @BeforeAll
    public static void startServer() {
        mockserver = ClientAndServer.startClientAndServer(8080);
    }

    @AfterAll
    public static void stopServer() {
        mockserver.stop();
    }

    @BeforeAll
    private static void createExpectations() {
        questions.add(question);
        questions.add(question1);
        questions.add(question2);
        answeredQuestions.add(question2);
        User.setInstance(u);
        // {GET} /session/token valid request
        mockserver.when(request().withMethod("GET")
                .withPath("/session/" + uuid))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(gson.toJson(session)));
        // {POST} session/{token}/user/ valid request
        mockserver.when(request()
                    .withMethod("POST")
                    .withPath("/session/{sessionToken}/user/")
                    .withPathParameter(Parameter.param("sessionToken", uuidRegexPattern))
                    .withBody(gson.toJson(u)))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(gson.toJson(u)));
        // {POST} /session/ valid request
        mockserver.when(request()
                .withMethod("POST")
                .withPath("/session/")
                .withBody(gson.toJson(session2)))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(gson.toJson(session2)));
        mockserver.when(request()
                .withMethod("POST")
                .withPath("/session/"))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(gson.toJson(session)));
        mockserver.when(request()
                .withMethod("GET")
                .withPath("/question/")
                .withHeader("X-USER-TOKEN", u.getToken().toString()))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(gson.toJson(questions)));
        // {POST} /upvote/ valid request
        mockserver.when(request()
                .withMethod("POST")
                .withPath("/question/1/upvote"))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(gson.toJson(upvote)));
        // {DELETE} /upvote/{id} valid request
        mockserver.when(request()
                .withMethod("DELETE")
                .withPathParameter(Parameter.param("id"))
                .withPath("/upvote/{id}/"))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(gson.toJson(upvote)));
        mockserver.when(request()
                .withMethod("POST")
                .withPath("/question/")
                .withHeader("X-USER-TOKEN", u.getToken().toString()))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(gson.toJson(question)));
        mockserver.when(request()
                .withMethod("GET")
                .withPath("/question/" + u.getId())
                .withHeader("X-USER-TOKEN", u.getToken().toString()))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(gson.toJson(questions)));
        mockserver.when(request()
                .withMethod("DELETE")
                .withPath("/question/" + question.getId())
                .withHeader("X-USER-TOKEN", u.getToken().toString()))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(gson.toJson(question)));
        mockserver.when(request()
                .withMethod("GET")
                .withPath("/session/speed")
                .withHeader("X-USER-TOKEN", u.getToken().toString()))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(gson.toJson(List.of(1,2,3,4,5))));
        mockserver.when(request()
                .withMethod("POST")
                .withPath("/session/speed")
                .withHeader("X-USER-TOKEN", u.getToken().toString()))
                .respond(response()
                        .withStatusCode(200));
        mockserver.when(request()
                .withMethod("GET")
                .withPath("/event/")
                .withHeader("X-USER-TOKEN", u.getToken().toString()))
                .respond(response()
                        .withStatusCode(200)
                        .withBody("Test"));
        mockserver.when(request()
                .withMethod("GET")
                .withPath("/question/export")
                .withHeader("X-USER-TOKEN", u.getToken().toString()))
                .respond(response()
                        .withStatusCode(200)
                        .withBody("Test"));
        mockserver.when(request()
                .withMethod("GET")
                .withPath("/question/status/true")
                .withHeader("X-USER-TOKEN", u.getToken().toString()))
                .respond(response()
                         .withStatusCode(200)
                         .withBody(gson.toJson(answeredQuestions)));
    }

    @Test
    public void testGetSessionByToken() throws ExecutionException, InterruptedException {
        assertEquals(session,serverCommunication.getSessionByToken(uuid.toString()).get());
    }

    @Test
    public void createUserTest() throws ExecutionException, InterruptedException {
        assertEquals(u,serverCommunication.createUser(session.getStudentToken(), u).get());
    }

    @Test
    public void createSessionTest() throws ExecutionException, InterruptedException {
        assertEquals(session, serverCommunication.createSession(session.getSessionName()).get());
    }

    @Test
    public void badRequest() throws ExecutionException, InterruptedException {
        try {
            serverCommunication.getSessionByToken("abc").get();
        } catch (ExecutionException baseException) {
            Throwable cause = baseException.getCause();
            assertEquals(ServerResponseException.class, cause.getClass());
            ServerResponseException responseException = (ServerResponseException) cause;
            assertEquals(404, responseException.getResponse().statusCode());
        }
    }

    @Test
    public void getQuestions() throws ExecutionException, InterruptedException {
        assertEquals(questions,serverCommunication.getQuestions().get());
    }

    @Test
    public void createUpvoteTest() throws ExecutionException, InterruptedException {
        assertTrue(serverCommunication.createUpvote(question).get() instanceof Upvote);
    }

    @Test
    public void createQuestion() throws ExecutionException, InterruptedException {
        assertEquals(question,serverCommunication.createQuestion(question).get());
    }

    @Test
    public void getQuestionsUser() throws ExecutionException, InterruptedException {
        User.setInstance(u);
        assertEquals(questions,serverCommunication.getQuestionsUser(u.getId()).get());
    }

    @Test
    public void deleteQuestion() throws ExecutionException, InterruptedException {
        assertEquals(question,serverCommunication.deleteQuestion(question).get());
    }

    @Test
    public void getSpeedFeedback() throws ExecutionException, InterruptedException {
        assertEquals(List.of(1,2,3,4,5), serverCommunication.getSpeedFeedback().get());
    }

    @Test
    public void setSpeedFeedback() throws ExecutionException, InterruptedException {
        serverCommunication.setSpeedFeedback(5);
    }

    @Test
    public void getLogs() throws ExecutionException, InterruptedException {
        assertEquals("Test",serverCommunication.getLogs().get());
    }

    @Test
    public void exportQuestios() throws ExecutionException, InterruptedException {
        assertEquals("Test",serverCommunication.exportQuestions().get());
    }

    @Test
    public void banUser() throws ExecutionException, InterruptedException {
        serverCommunication.banUser(u);
    }

    @Test
    public void createPoll() {
        List<String> strings = new ArrayList<>();
        Poll poll = new Poll("Test", strings);
        serverCommunication.createPoll(poll);
    }

    @Test
    public void changeStatus() {
        Question question = new Question("Text");
        serverCommunication.changeStatus(question,false);
    }

    @Test
    public void updateQuestion() {
        Question question = new Question("Text");
        serverCommunication.updateQuestion(question);
    }

    @Test
    public void setVote() {
        serverCommunication.setVote(poll,new Poll.PollOption("a"));
    }

    @Test
    public void doRefresh() {
        serverCommunication.doAutomaticRefresh(null);
    }

    @Test
    public void closePoll() {
        serverCommunication.closePoll(poll);
    }

    @Test
    public void reopenPoll() {
        serverCommunication.reopenPoll(poll);
    }

    @Test
    public void closeSession() {
        serverCommunication.closeSession();
    }

    @Test
    public void scheduleSession() throws ExecutionException, InterruptedException {
        assertEquals(session2.getDate(),serverCommunication.scheduleSession(
            session2.getSessionName(),session2.getDate()).get().getDate());
    }

}

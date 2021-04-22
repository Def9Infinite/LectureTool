package nl.tudelft.oopp.project.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import nl.tudelft.oopp.project.Poll;
import nl.tudelft.oopp.project.Question;
import nl.tudelft.oopp.project.Session;
import nl.tudelft.oopp.project.Upvote;
import nl.tudelft.oopp.project.User;
import nl.tudelft.oopp.project.exceptions.NoExistingUserInstance;
import nl.tudelft.oopp.project.exceptions.ServerResponseException;

public class ServerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();
    private static Gson gson = new Gson();
    private static String serverLocation = "http://localhost:8080/";

    public ServerCommunication(HttpClient client) {
        ServerCommunication.client = client;
    }

    private static CompletableFuture<HttpResponse<String>> callServer(String method, String path, String body) {
        return callServer(method, path, body, false);
    }

    // TODO make method an ENUM containing possible methods.
    private static CompletableFuture<HttpResponse<String>> callServer(String method, String path, String body,
                                                                      boolean includeAuthorization) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(serverLocation + path))
                .setHeader("Accept", "application/json")
                .setHeader("Content-type", "application/json");

        if (method.equals("POST")) {
            requestBuilder = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
        } else if (method.equals("GET")) {
            requestBuilder = requestBuilder.GET();
        } else if (method.equals(("DELETE"))) {
            requestBuilder = requestBuilder.DELETE();
        } else {
            requestBuilder = requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body));
        }

        if (includeAuthorization) {
            try {
                User userInstance = User.getInstance();
                requestBuilder = requestBuilder.setHeader("X-USER-TOKEN", userInstance.getToken().toString());
            } catch (NoExistingUserInstance noExistingUserInstance) {
                noExistingUserInstance.printStackTrace();
            }
        }

        return client
                .sendAsync(requestBuilder.build(), HttpResponse.BodyHandlers.ofString())
                .thenApply(ServerCommunication::checkServerResponse);
    }

    /**
     * Handles unexpected server responses.
     * @param response - supplied from callServer async calls.
     * @return the checked response.
     */
    private static HttpResponse<String> checkServerResponse(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            throw new ServerResponseException(response);
        }
        return response;
    }

    /**
     * GETs data from the server.
     * @param path location of data
     * @return server response
     */
    public static CompletableFuture<HttpResponse<String>> get(String path) {
        return get(path, false);
    }

    /**
     * GETs data from the server.
     * @param path location of data
     * @return server response
     */
    public static CompletableFuture<HttpResponse<String>> get(String path, boolean includeAuthorization) {
        return callServer("GET", path, "", includeAuthorization);
    }

    /**
     * POSTs data to the server.
     * @param path server location from root
     * @param body data to post
     * @return server response
     */
    public static CompletableFuture<HttpResponse<String>> post(String path, String body) {
        return post(path, body, false);
    }

    /**
     * POSTs data to the server.
     * @param path server location from root
     * @param body data to post
     * @return server response
     */
    public static CompletableFuture<HttpResponse<String>> post(String path, String body, boolean includeAuthorization) {
        return callServer("POST", path, body, includeAuthorization);
    }

    /**
     * PUTs data to the server.
     * @param path server location from root
     * @param body data to post
     * @return server response
     */
    public static CompletableFuture<HttpResponse<String>> put(String path, String body) {
        return put(path,body,false);
    }

    /**
     * PUTs data to the server.
     * @param path server location from root
     * @param body data to post
     * @return server response
     */
    public static CompletableFuture<HttpResponse<String>> put(String path, String body, boolean includeAuthorization) {
        return callServer("PUT",path,body,includeAuthorization);
    }

    /**
     * DELETEs data from the server.
     * @param path server location from root
     * @param body data to delete
     * @return server response
     */
    public static CompletableFuture<HttpResponse<String>> delete(String path, String body) {
        return delete(path, body, false);
    }

    /**
     * DELETEs data from the server.
     * @param path server location from root
     * @param body data to delete
     * @return server response
     */
    public static CompletableFuture<HttpResponse<String>> delete(String path,
                                     String body, boolean includeAuthorization) {
        return callServer("DELETE", path, body, includeAuthorization);
    }

    public static CompletableFuture<HttpResponse<String>> delete(String path, boolean includeAuthorization) {
        return callServer("DELETE", path, "", includeAuthorization);

    }


    /**
     * Connects to the post method endpoint on the server and in response gets a session object.
     *
     * @param sessionName name of session that should be created
     * @return the session that was just created.
     */
    public static CompletableFuture<Session> createSession(String sessionName) {
        Map<String, String> body = Map.of("sessionName", sessionName);
        return post("session/", gson.toJson(body)).thenApply(response -> {
            return response != null ? gson.fromJson(response.body(), Session.class) : null;
        });
    }

    /**
     * Get request for session using the token.
     *
     * @param token either mod token or student token
     * @return the session which has the token
     */
    public static CompletableFuture<Session> getSessionByToken(String token) {
        return get("session/" + token).thenApply(response -> {
            return response != null ? gson.fromJson(response.body(), Session.class) : null;
        });
    }

    /**
     * Put request to save user on the server.
     *
     * @param u user to be saved
     * @return the user
     */
    public static CompletableFuture<User> createUser(UUID sessionToken, User u) {
        String path = "session/" + sessionToken.toString() + "/user/";
        return post(path, gson.toJson(u)).thenApply(response -> {
            return response != null ? gson.fromJson(response.body(), User.class) : null;
        });
    }

    /**
     * Creates a new question on the server.
     *
     * @param q question to be created
     * @return created question
     */
    public static CompletableFuture<Question> createQuestion(Question q) {
        return post("question/", gson.toJson(q), true).thenApply(response -> {
            return response != null ? gson.fromJson(response.body(), Question.class) : null;
        });
    }

    /**
     * Get request for return a list of questions in current session.
     * @return a list of questions
     */
    public static CompletableFuture<List<Question>> getQuestions() {
        return get("question/",true).thenApply(response -> {
            return response != null ? gson.fromJson(response.body(),
                                                    new TypeToken<List<Question>>(){}.getType()) : null;
        });
    }

    /**
     * Get request for return a list of answered questions in current session.
     * @return a list of questions
     */
    public static CompletableFuture<List<Question>> getAnsweredQuestions() {
        return get("question/answered/",true).thenApply(response -> {
            return response != null ? gson.fromJson(response.body(),
                    new TypeToken<List<Question>>(){}.getType()) : null;
        });
    }

    /**
     * Find session by User Token.
     * @param token usertoken for the user
     * @return the session its in
     */
    public static CompletableFuture<Session> getSessionByUserToken(String token) {
        return get("user/" + token + "/session/").thenApply(response -> {
            return response != null ? gson.fromJson(response.body(), Session.class) : null;
        });
    }

    /**
     * Creates a new upvote on the server.
     * @param q - the question from which the upvote will be created
     * @return created upvote
     */
    public static CompletableFuture<Upvote> createUpvote(Question q) {
        String path = "question/" + q.getId() + "/upvote";
        return post(path, "", true).thenApply(response -> {
            return response != null ? gson.fromJson(response.body(), Upvote.class) : null;
        });
    }

    /**
     * Deletes an upvote from the server.
     * @param q - the question from which the upvote will be deleted
     * @return the deleted upvote
     */
    public static CompletableFuture<Upvote> deleteUpvote(Question q) {
        String path = "question/" + q.getId() + "/upvote";
        return delete(path, "", true).thenApply(response -> {
            return response != null ? gson.fromJson(response.body(), Upvote.class) : null;
        });
    }

    /**
     * Delete the question q.
     * @param question question to be deleted
     **/
    public static CompletableFuture<Question> deleteQuestion(Question question) {
        return delete("question/" + question.getId(), true).thenApply(response -> {
            return response != null ? gson.fromJson(response.body(), Question.class) : null;
        });
    }

    /**
     * Get all questions from the user making the request.
     * @param id id of user
     * @return list of questions
     */
    public static CompletableFuture<List<Question>> getQuestionsUser(long id) {
        return get("question/" + id, true).thenApply(response -> {
            Type listType = new TypeToken<ArrayList<Question>>(){}.getType();
            return response != null ? gson.fromJson(response.body(), listType) : null;
        });
    }

    /**
     * Get a string representation of all events.
     * @return string representation
     */
    public static CompletableFuture<String> getLogs() {
        return get("event/",true).thenApply(response -> {
            return response != null ? response.body() : null;
        });
    }

    /**
     * Mark status of a question answered or not.
     * @param q the question
     * @param status the status of the question
     */
    public static void changeStatus(Question q, boolean status) {
        put("question/" + q.getId(),Boolean.toString(status),true);
    }

    public static void editQuestion(Question q, String text) {
        put("session/" + q.getUser().getSessionId() + "/question/" + q.getId(), text,true);
    }

    /**
     * Ban a user!.
     * @param userToBeBanned user object that should be banned.
     */
    public static void banUser(User userToBeBanned) {
        post("user/ban", gson.toJson(userToBeBanned), true);
    }

    /**
     * Gets the speedfeedback from the server.
     * @return list of speeeeeds
     */
    public static CompletableFuture<List<Integer>> getSpeedFeedback() {
        return get("session/speed", true).thenApply(response -> {
            Type listType = new TypeToken<ArrayList<Integer>>(){}.getType();
            return response != null ? gson.fromJson(response.body(), listType) : null;
        });
    }

    /**
     * Updates our users speed feedback.
     * @param speedFeedback value of speed
     */
    public static void setSpeedFeedback(int speedFeedback) {
        post("session/speed", String.valueOf(speedFeedback), true);
    }

    /**
     * Updates question.
     */
    public static void updateQuestion(Question question) {
        put("question", gson.toJson(question), true);
    }


    /**
     * Get csv string representation of all questions.
     * @return string retrieved from server.
     */
    public static CompletableFuture<String> exportQuestions() {
        return get("question/export", true).thenApply(response -> {
            return response != null ? response.body() : null;
        });
    }

    public static void createPoll(Poll poll) {
        post("poll", gson.toJson(poll), true);
    }

    /**
     * Gets the list of polls for this session from the server.
     * @return completable future which will contain a list of polls.
     */
    public static CompletableFuture<List<Poll>> getPolls() {
        return get("/poll", true).thenApply(response -> {
            Type listType = new TypeToken<ArrayList<Poll>>(){}.getType();
            return response != null ? gson.fromJson(response.body(), listType) : null;
        });
    }

    /**
     * Updates poll with vote.
     * @param poll to vote on
     * @param pollOption option to vote for
     */
    public static void setVote(Poll poll, Poll.PollOption pollOption) {
        put("/poll/" + poll.getId(), String.valueOf(pollOption.getId()), true);
    }

    /**
     * Close poll.
     * @param poll to be closed
     */
    public static void closePoll(Poll poll) {
        post("/poll/" + poll.getId() + "/toggle", "false", true);
    }

    /**
     * reopen poll.
     * @param poll to be reopened
     */
    public static void reopenPoll(Poll poll) {
        post("/poll/" + poll.getId() + "/toggle", "true", true);
    }

    public static void closeSession() {
        post("/session/close", "", true);
    }

    /**
    * Polls the server to know if we should update.
     * @param callbacks Called on server response.
     **/
    public static void doAutomaticRefresh(Map<String, Runnable> callbacks) {
        ForkJoinPool.commonPool().submit(() -> {
            while (true) {
                HttpResponse<String> pollingResponse = get("session/poll", true).completeOnTimeout(
                        null, 35, TimeUnit.SECONDS).get();
                if (pollingResponse != null && pollingResponse.body() != null) {
                    String pollingResult = pollingResponse.body();
                    if (callbacks.containsKey(pollingResult)) {
                        callbacks.get(pollingResult).run();
                    }
                }

            }
        });
    }

    /**
     * Create a session with a date represented as string.
     * @param sessionName name of the session
     * @param date string representation of date session starts at
     * @return created session
     */
    public static CompletableFuture<Session> scheduleSession(String sessionName, String date) {
        Session session = new Session(sessionName,date);
        return post("session/", gson.toJson(session)).thenApply(response -> {
            return response != null ? gson.fromJson(response.body(), Session.class) : null;
        });
    }

    /**
     * Get the time the session takes place.
     * @param token token to join session
     * @return date in string format
     */
    public static CompletableFuture<String> getSchedule(String token) {
        return get("session/" + token + "/time", false).thenApply(response -> {
            return response != null ? response.body() : null;
        });
    }
}
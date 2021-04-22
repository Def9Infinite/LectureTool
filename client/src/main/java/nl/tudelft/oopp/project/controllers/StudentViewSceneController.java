package nl.tudelft.oopp.project.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;

import javafx.scene.control.Tab;
import nl.tudelft.oopp.project.Poll;
import nl.tudelft.oopp.project.Question;
import nl.tudelft.oopp.project.User;
import nl.tudelft.oopp.project.communication.ServerCommunication;
import nl.tudelft.oopp.project.exceptions.NoExistingUserInstance;
import nl.tudelft.oopp.project.exceptions.ServerResponseException;
import nl.tudelft.oopp.project.views.AnsweredQuestionListViewCell;
import nl.tudelft.oopp.project.views.PollTab;
import nl.tudelft.oopp.project.views.QuestionListViewCell;
import nl.tudelft.oopp.project.views.YourQuestionListViewCell;

public class StudentViewSceneController implements Initializable {
    @FXML
    private JFXTextArea questionTextField;
    @FXML
    private JFXButton askQuestionButton;
    @FXML
    private ListView<Question> questionList;
    @FXML
    private ListView<Question> answeredQuestionList;
    @FXML
    private SplitPane myPane;
    @FXML
    private ListView<Question> yourQuestionList;
    @FXML
    private JFXSnackbar snackbarContainer;
    @FXML
    private JFXTabPane pollTabPane;

    private static ObservableList<Question> questionsObservableList;
    private static ObservableList<Question> yourQuestionObservableList;
    private static ObservableList<Question> answeredQuestionObservableList;
    private static Long lastQuestionChange;


    /**
     * Constructor for the student view scene controller.
     * At start up, add all current questions to listview.
     * @throws ExecutionException completable future error
     * @throws InterruptedException completable future error
     */
    public StudentViewSceneController() throws ExecutionException, InterruptedException {
        questionsObservableList = FXCollections.observableArrayList();
        answeredQuestionObservableList = FXCollections.observableArrayList();
        yourQuestionObservableList = FXCollections.observableArrayList();
        answeredQuestionObservableList = FXCollections.observableArrayList();
    }

    /**
     * Shows a snackbar at the bottom of the screen.
     * @param text snackbar display text
     */
    public void showSnackbar(String text) {
        Label test = new Label(text);
        test.setPadding(new Insets(0,20,0,20));
        test.getStyleClass().add("snackbar-label");
        snackbarContainer.enqueue(new JFXSnackbar.SnackbarEvent(test));
    }

    private void refreshCells() {
        questionList.refresh();
        answeredQuestionList.refresh();
        yourQuestionList.refresh();
    }

    /**
     * Ask Question Handler.
     * @param actionEvent click event
     */
    public void askQuestion(ActionEvent actionEvent) {
        if (questionTextField == null || questionTextField.getText() == null
                || questionTextField.getText().length() == 0) {
            showSnackbar("Write a question first!");
            return;
        } else if (questionTextField.getText().length() > 250) {
            showSnackbar("Questions have to be less than 250 characters. Try to be more concrete!");
            return;
        }
        Question localQuestion = new Question(questionTextField.getText());
        ServerCommunication.createQuestion(localQuestion).handle((question, exception) -> {
            if (exception != null) {
                CompletionException completionException = (CompletionException) exception;
                if (completionException.getCause() instanceof ServerResponseException) {
                    ServerResponseException sre = (ServerResponseException) completionException.getCause();
                    if (sre.getResponse().statusCode() == 403) {
                        Platform.runLater(() -> showSnackbar("You have been banned from this session."));
                    } else if (sre.getResponse().statusCode() == 410) {
                        Platform.runLater(() -> showSnackbar("Cannot ask question, session is closed."));
                    } else {
                        Platform.runLater(() -> showSnackbar("Could not ask question, an unknown error occurred."));
                    }
                    return null;
                }
            }
            return question;
        }).thenAcceptAsync(question -> {
            if (question != null) {
                Platform.runLater(() -> questionTextField.setText("")); // Remove question text from textarea.
            }
            try {
                refreshQuestions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Update the questions.
     * @throws ExecutionException error
     * @throws InterruptedException error
     */
    public void refreshQuestions() {
        changeQuestions();
        initPolls();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myPane.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                refreshCells();
            }
        });
        questionList.setItems(questionsObservableList);
        questionList.setCellFactory(questionListView -> new QuestionListViewCell());
        yourQuestionList.setItems(yourQuestionObservableList);
        yourQuestionList.setCellFactory(yourQuestionListView -> new YourQuestionListViewCell());
        answeredQuestionList.setItems(answeredQuestionObservableList);
        answeredQuestionList.setCellFactory(answeredQuestionListView -> new AnsweredQuestionListViewCell());
        try {
            refreshQuestions();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initPolls();
        startAutomaticRefresh();
    }

    /**
     * Set up polls.
     */
    public void initPolls() {
        ServerCommunication.getPolls().thenAccept(polls -> {
            Platform.runLater(() -> setPolls(polls));
        });
    }

    /**
     * Sets poll tabs.
     * @param polls to set to
     */
    public void setPolls(List<Poll> polls) {
        pollTabPane.getTabs().clear();
        int i = polls.size();
        for (Poll p : polls) {
            pollTabPane.getTabs().add(new PollTab(pollTabPane, p, i--, false));
        }
    }

    /**
     * Refresh the polls.
     */
    public void refreshPolls() {
        ServerCommunication.getPolls().thenAccept(polls -> {
            List<PollTab> pollTabList = pollTabPane.getTabs().stream().filter(t -> t instanceof PollTab)
                                            .map(t -> (PollTab) t).collect(Collectors.toList());
            if (polls.size() != pollTabList.size()) {
                Platform.runLater(() -> setPolls(polls));
            }
            for (int i = 0; i < polls.size(); i++) {
                PollTab pollTab = pollTabList.get(i);
                Poll poll = polls.get(i);
                if (pollTab.getPoll().getId() != poll.getId()) {
                    Platform.runLater(() -> setPolls(polls));
                    break;
                }
                Platform.runLater(() -> pollTab.updatePoll(poll));
            }
        });
    }


    /**
     * Add all items to the observable list that should be there and remove all items that no longer exist.
     */
    public static void changeQuestions() {
        if (lastQuestionChange != null &&  lastQuestionChange > (System.currentTimeMillis() - 150)) {
            return; // A very poor way of preventing too many refreshes.
        }
        User clientUser;
        try {
            clientUser = User.getInstance();
        } catch (NoExistingUserInstance noExistingUserInstance) {
            noExistingUserInstance.printStackTrace();
            return;
        }
        /// Refresh all questions from default questions tab
        ServerCommunication.getQuestions().thenAcceptAsync(questions ->
                Platform.runLater(() -> {
                    questionsObservableList.clear();
                    questionsObservableList.addAll(questions);
                }));
        // Refresh questions from "Your Questions" tab
        ServerCommunication.getQuestionsUser(clientUser.getId()).thenAcceptAsync(questions -> {
            Platform.runLater(() -> {
                yourQuestionObservableList.clear();
                yourQuestionObservableList.addAll(questions);
            });
        });
        // Refresh questions from "Answered Questions" tab
        ServerCommunication.getAnsweredQuestions().thenAcceptAsync(questions ->
                Platform.runLater(() -> {
                    answeredQuestionObservableList.clear();
                    answeredQuestionObservableList.addAll(questions);
                }));
    }

    /**
     * Starts a seperate thread which will poll the server and check for updates.
     */
    public void startAutomaticRefresh() {
        Map<String, Runnable> callbacks = new HashMap<>();
        callbacks.put("refresh_questions", StudentViewSceneController::changeQuestions);
        callbacks.put("refresh_polls", this::refreshPolls);
        ServerCommunication.doAutomaticRefresh(callbacks);
    }

    /**
     * Handles sending speed feedback.
     * @param speed value of speed.
     */
    public void sendSpeedFeedback(int speed) {
        ServerCommunication.setSpeedFeedback(speed);
        showSnackbar("Lecture speed feedback sent!");
    }

    public void verySlowSpeedButton() {
        sendSpeedFeedback(0);
    }

    public void slowSpeedButton() {
        sendSpeedFeedback(1);
    }

    public void goodSpeedButton() {
        sendSpeedFeedback(2);
    }

    public void fastSpeedButton() {
        sendSpeedFeedback(3);
    }

    public void veryFastSpeedButton() {
        sendSpeedFeedback(4);
    }
}

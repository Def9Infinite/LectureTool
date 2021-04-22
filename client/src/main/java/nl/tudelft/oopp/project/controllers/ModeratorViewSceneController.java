package nl.tudelft.oopp.project.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.project.Poll;
import nl.tudelft.oopp.project.Question;
import nl.tudelft.oopp.project.Session;
import nl.tudelft.oopp.project.communication.ServerCommunication;
import nl.tudelft.oopp.project.exceptions.NoExistingSessionInstance;
import nl.tudelft.oopp.project.views.AnsweredQuestionListViewCell;
import nl.tudelft.oopp.project.views.PollCreateOption;
import nl.tudelft.oopp.project.views.PollTab;
import nl.tudelft.oopp.project.views.QuestionListModeratorViewCell;

public class ModeratorViewSceneController implements Initializable {

    @FXML
    private Button switchButton;
    @FXML
    private ListView<Question> questionList;
    @FXML
    private TextField studentToken;
    @FXML
    private TextField moderatorToken;
    @FXML
    private BarChart<String, Number> lectureSpeedChart;
    @FXML
    private JFXSnackbar snackbarContainer;
    @FXML
    private ListView<Question> answeredQuestionList;
    @FXML
    private JFXTabPane pollTabPane;
    @FXML
    private VBox pollCreateListContainer;
    @FXML
    private JFXButton createPollButton;
    @FXML
    private JFXTextField newPollQuestionInput;

    private static ObservableList<Question> answeredQuestionObservableList;
    private static ObservableList<Question> questionsObservableList;
    private static Long lastQuestionChange;
    private static XYChart.Series<String, Number> speedSeries;


    /**
     * Initialize observeable lists.
     * @throws ExecutionException JavaFX
     * @throws InterruptedException JavaFX
     */
    public ModeratorViewSceneController() throws ExecutionException, InterruptedException {
        questionsObservableList = FXCollections.observableArrayList();
        answeredQuestionObservableList = FXCollections.observableArrayList();
    }


    /**
     * Switch view to moderator view.
     *
     * @throws IOException when file could not been found
     */
    public void switchView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LecturerViewScene.fxml"));
        Scene currentScene = (switchButton).getScene();
        Stage stage = (Stage) currentScene.getWindow();
        Scene newScene = new Scene(loader.load(), currentScene.getWidth(), currentScene.getHeight());
        stage.setScene(newScene);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            studentToken.setText("Student Token = " + Session.getInstance().getStudentToken());
            moderatorToken.setText("Moderator Token = " + Session.getInstance().getPowerToken());
        } catch (NoExistingSessionInstance e) {
            e.printStackTrace();
        }
        questionList.setItems(questionsObservableList);
        questionList.setCellFactory(questionListView -> new QuestionListModeratorViewCell(this));
        answeredQuestionList.setItems(answeredQuestionObservableList);
        answeredQuestionList.setCellFactory(answeredQuestionListView -> new AnsweredQuestionListViewCell());
        speedSeries = new XYChart.Series<>();

        try {
            changeQuestions();
            initSpeed();
            initPolls();
        } catch (Exception e) {
            e.printStackTrace();
        }
        startAutomaticRefresh();
    }


    /**
     * Creates initial speed bars.
     */
    public void initSpeed() {
        speedSeries.getData().add(new XYChart.Data<>("Too slow", 0));
        speedSeries.getData().add(new XYChart.Data<>("slow", 0));
        speedSeries.getData().add(new XYChart.Data<>("Good", 0));
        speedSeries.getData().add(new XYChart.Data<>("Fast", 0));
        speedSeries.getData().add(new XYChart.Data<>("Too fast", 0));
        lectureSpeedChart.getData().add(speedSeries);
        updateSpeed();
    }

    /**
     * Updates speed bars.
     */
    public static void updateSpeed() {
        ServerCommunication.getSpeedFeedback().thenAcceptAsync(speeds -> {
            Platform.runLater(() -> {
                speedSeries.getData().get(0).setYValue(speeds.get(0));
                speedSeries.getData().get(1).setYValue(speeds.get(1));
                speedSeries.getData().get(2).setYValue(speeds.get(2));
                speedSeries.getData().get(3).setYValue(speeds.get(3));
                speedSeries.getData().get(4).setYValue(speeds.get(4));
            });
        });
    }

    public void initPolls() {
        addPollOption();
        addPollResults();
    }

    /**
     * Refreshes all the polls with new data from the server.
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
     * Add current polls.
     */
    public void addPollResults() {
        ServerCommunication.getPolls().thenAccept(polls -> {
            Platform.runLater(() -> setPolls(polls));
        });
    }

    /**
     * Removes all polls and creates new ones.
     * @param polls new polls
     */
    public void setPolls(List<Poll> polls) {
        pollTabPane.getTabs().removeIf(t -> t instanceof PollTab);
        int i = polls.size();
        for (Poll p : polls) {
            pollTabPane.getTabs().add(new PollTab(pollTabPane, p, i--, true));
        }
    }

    /**
     * Creates new create poll option.
     */
    public void addPollOption() {
        pollCreateListContainer.getChildren().add(new PollCreateOption(this, pollCreateListContainer));
    }

    /**
     * Creates new poll based on the available option nodes.
     */
    public void createNewPoll() {
        ObservableList<Node> optionList = pollCreateListContainer.getChildren();
        List<String> pollOptions = optionList.stream()
                .map(o -> ((PollCreateOption) o).getText())
                .filter(Objects::nonNull)
                .filter(t -> !(t.length() < 1))
                .collect(Collectors.toList());
        String questionText = newPollQuestionInput.getText();
        if (questionText == null || questionText.length() < 1) {
            showSnackbar("A poll needs a question.");
            return;
        } else if (pollOptions.size() < 2) {
            showSnackbar("A poll needs at least 2 possible answers.");
            return;
        }
        Poll poll = new Poll(questionText, pollOptions);
        ServerCommunication.createPoll(poll);
    }

    /**
     * Update questions.
     */
    public static void changeQuestions() {
        if (lastQuestionChange != null &&  lastQuestionChange > (System.currentTimeMillis() - 150)) {
            return; // A very poor way of preventing too many refreshes.
        }
        lastQuestionChange = System.currentTimeMillis();
        ServerCommunication.getQuestions().thenAcceptAsync(questions -> {
            Platform.runLater(() -> {
                questionsObservableList.clear();
                questionsObservableList.addAll(questions);
            });
        });
        ServerCommunication.getAnsweredQuestions().thenAcceptAsync(questions -> {
            Platform.runLater(() -> {
                answeredQuestionObservableList.clear();
                answeredQuestionObservableList.addAll(questions);
            });
        });
    }

    /**
     * Update the questions.
     * @throws ExecutionException error
     * @throws InterruptedException error
     */
    public void refreshQuestions() {
        changeQuestions();
        updateSpeed();
    }

    /**
     * Create File and write the string retrieved using getLog() to the file.
     */
    public void createLog() {
        ServerCommunication.getLogs().thenAcceptAsync(string -> {

            try {
                File dir = new File("SessionLogs");
                dir.mkdirs();
                File output = new File(dir,"SessionLog_SessionId_" + Session.getInstance().getId() + ".txt");
                output.createNewFile();
                FileWriter fw = new FileWriter(output);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                fw.write("Log from Session: " + Session.getInstance().getId()
                        + " Created at: " + timestamp + "\n\n");
                fw.write(string);
                fw.flush();
                fw.close();
                Platform.runLater(() -> showSnackbar("Succesfully created session log file"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create file in directory questionExports which is a csv file with all questions with their upvotes.
     */
    public void exportQuestions() {
        ServerCommunication.exportQuestions().thenAcceptAsync(string -> {
            try {
                File dir = new File("QuestionExports");
                dir.mkdirs();
                File output = new File(dir,"ExportQuestions_SessionId_" + Session.getInstance().getId() + ".csv");
                output.createNewFile();
                FileWriter fw = new FileWriter(output);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                fw.write("Export from Session: " + Session.getInstance().getId()
                        + " Created at: " + timestamp + System.getProperty("line.separator"));
                fw.write("Upvotes, Question Text, Is Answered, Answer, Asked By, Time");
                fw.write(System.getProperty("line.separator"));
                fw.write(string);
                fw.flush();
                fw.close();
                Platform.runLater(() -> showSnackbar("Successfully exported questions"));
            } catch (Exception e) {
                e.printStackTrace();
            }


        });
    }

    /**
     * Handle session close.
     */
    public void closeSession() {
        ServerCommunication.closeSession();
        showSnackbar("Closing session. Students can no longer join or ask questions.");
    }

    /**
     * Show a snackbar to let users know it worked.
     * @param text text that will be shown
     */
    public void showSnackbar(String text) {
        Label test = new Label(text);
        test.setPadding(new Insets(0,20,0,20));
        test.getStyleClass().add("snackbar-label");
        snackbarContainer.enqueue(new JFXSnackbar.SnackbarEvent(test));
    }

    /**
     * Starts a seperate thread which will poll the server and check for updates.
     */
    public void startAutomaticRefresh() {
        Map<String, Runnable> callbacks = new HashMap<>();
        callbacks.put("refresh_questions", ModeratorViewSceneController::changeQuestions);
        callbacks.put("refresh_speed_feedback", ModeratorViewSceneController::updateSpeed);
        callbacks.put("refresh_polls", this::refreshPolls);
        ServerCommunication.doAutomaticRefresh(callbacks);
    }

    public static ObservableList<Question> getQuestionsObservableList() {
        return questionsObservableList;
    }
}

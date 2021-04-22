package nl.tudelft.oopp.project.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.tudelft.oopp.project.Question;
import nl.tudelft.oopp.project.Session;
import nl.tudelft.oopp.project.communication.ServerCommunication;
import nl.tudelft.oopp.project.views.AnsweredQuestionListViewCell;
import nl.tudelft.oopp.project.views.QuestionListLectureViewCell;

public class LecturerViewSceneController implements Initializable {

    @FXML
    private Button switchButton;
    @FXML
    private ListView<Question> questionList;
    @FXML
    private BarChart<String, Number> lectureSpeedChart;

    private static ObservableList<Question> questionsObservableList;
    private static Long lastQuestionChange;
    private static XYChart.Series<String, Number> speedSeries;


    public LecturerViewSceneController() throws ExecutionException, InterruptedException {
        questionsObservableList = FXCollections.observableArrayList();
    }


    /**
     * Update questions.
     */
    public static void changeQuestions() {
        if (lastQuestionChange != null &&  lastQuestionChange > (System.currentTimeMillis() - 150)) {
            return; // A very poor way of preventing too many refreshes.
        }
        ServerCommunication.getQuestions().thenAcceptAsync(questions -> {
            Platform.runLater(() -> {
                questionsObservableList.clear();
                questionsObservableList.addAll(questions);
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

    /**
     * Switch view to moderator view.
     * @throws IOException when file could not been found
     */
    public void switchView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModeratorViewScene.fxml"));
        Scene currentScene = (switchButton).getScene();
        Stage stage = (Stage) currentScene.getWindow();
        Scene newScene = new Scene(loader.load(), currentScene.getWidth(), currentScene.getHeight());
        stage.setScene(newScene);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        questionList.setItems(questionsObservableList);
        questionList.setCellFactory(questionListView -> new QuestionListLectureViewCell());
        speedSeries = new XYChart.Series<>();
        try {
            refreshQuestions();
            initSpeed();
            startAutomaticRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a seperate thread which will poll the server and check for updates.
     */
    public void startAutomaticRefresh() {
        Map<String, Runnable> callbacks = new HashMap<>();
        callbacks.put("refresh_questions", LecturerViewSceneController::changeQuestions);
        callbacks.put("refresh_speed_feedback", LecturerViewSceneController::updateSpeed);
        ServerCommunication.doAutomaticRefresh(callbacks);
    }
}


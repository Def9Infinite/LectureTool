package nl.tudelft.oopp.project.controllers;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nl.tudelft.oopp.project.Session;
import nl.tudelft.oopp.project.User;
import nl.tudelft.oopp.project.communication.ServerCommunication;


public class CreateSessionSceneController {
    @FXML
    private TextField nameField;
    @FXML
    private JFXButton createSessionButton;

    /**
     * Handles clicking the create session button.
     */
    public void createSession() throws UnknownHostException {
        ServerCommunication.createSession(nameField.getText()).thenAccept(session -> {
            try {
                switchToSessionCodesScene(session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void switchToSessionCodesScene(Session session) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SessionCodesScene.fxml"));
        Stage stage = (Stage) (createSessionButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        Platform.runLater(() -> {
            SessionCodesSceneController controller = loader.getController();
            stage.setScene(scene);
            controller.setData(session);
        });
    }

    public TextField getNameField() {
        return nameField;
    }

    /**
     * Switch scene to join session fxml.
     *
     * @throws IOException when resource can't be found
     */
    public void joinSession() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/JoinSessionScene.fxml"));
        Stage stage = (Stage) (createSessionButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    /**
     * Switch scene to how to join session fxml.
     *
     * @throws IOException when resource can't be found
     */
    public void howToCreateSession() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/HowToCreateSessionScene.fxml"));
        Stage stage = (Stage) (createSessionButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    /**
     * Create user on the server.
     *
     * @return Pending user request.
     * @throws UnknownHostException if DNS lookup fails.
     */
    public CompletableFuture<User> createUser(Session session, String nickname) throws UnknownHostException {
        UUID sessionToken = session.getPowerToken();
        User tempUser = new User(nickname);
        return ServerCommunication.createUser(sessionToken, tempUser);
    }

    /**
     * Switch view to Lecture view as default.
     *
     * @throws IOException when file cannot be found
     */
    public void moderatorView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModeratorViewScene.fxml"));
        Stage stage = (Stage) (createSessionButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        Platform.runLater(() -> {
            stage.setScene(scene);
        });
    }

    /**
     * Switch scene to schedule view.
     * @throws IOException when file cannot be found
     */
    public void scheduleSession() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DatePick.fxml"));
        Stage stage = (Stage) (createSessionButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        Platform.runLater(() -> {
            stage.setScene(scene);
        });
    }
}

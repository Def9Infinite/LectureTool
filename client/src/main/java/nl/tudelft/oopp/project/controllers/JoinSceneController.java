package nl.tudelft.oopp.project.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

import nl.tudelft.oopp.project.Session;
import nl.tudelft.oopp.project.User;
import nl.tudelft.oopp.project.communication.ServerCommunication;
import nl.tudelft.oopp.project.exceptions.ServerResponseException;


public class JoinSceneController {

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private JFXButton joinSessionButton;
    @FXML
    private JFXSnackbar snackbarContainer;

    /**
     * Get session from token, call create user.
     */
    public void joinSession() throws UnknownHostException {
        createUser().handle((createdUser, exception) -> {
            if (exception != null) {
                ServerResponseException responseException = (ServerResponseException) exception.getCause();
                switch (responseException.getResponse().statusCode()) {
                    case 403:
                        showSnackbar("You are not allowed to join this session.");
                        break;
                    case 404:
                        showSnackbar("Unknown invite code.");
                        break;
                    case 410:
                        showSnackbar("This session is closed.");
                        break;
                    case 423:
                        ServerCommunication.getSchedule(idField.getText()).thenAcceptAsync(time -> {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    showSnackbar("Session starts at: " + time);
                                }
                            });
                        });
                        break;
                    default:
                        showSnackbar("Unknown error occurred.");
                }
                return null;
            }
            return createdUser;
        }).thenAcceptAsync(createdUser -> {
            if (createdUser != null) {
                User.setInstance(createdUser);
                String userToken = createdUser.getToken().toString();
                ServerCommunication.getSessionByUserToken(userToken).thenAcceptAsync(session -> {
                    try {
                        Session.setInstance(session);
                        if (createdUser.getRole() == 0) {
                            studentView();
                        } else {
                            moderatorView();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    /**
     * Displays a snackbar in the join scene.
     * @param text display text
     */
    public void showSnackbar(String text) {
        Label test = new Label(text);
        test.setPadding(new Insets(0,20,0,20));
        test.getStyleClass().add("snackbar-label");
        snackbarContainer.enqueue(new JFXSnackbar.SnackbarEvent(test));
    }

    /**
     * Create user on the server.
     *
     * @return Pending user request.
     * @throws UnknownHostException if DNS lookup fails.
     */
    public CompletableFuture<User> createUser() throws UnknownHostException {
        UUID sessionToken;
        String nickName = nameField.getText();
        if (nickName == null || nickName.length() == 0) {
            showSnackbar("Name cannot be empty.");
            return null;
        }
        try {
            sessionToken = UUID.fromString(idField.getText());
        } catch (IllegalArgumentException e) {
            showSnackbar("Invite code is not a valid UUID.");
            return null;
        }
        User tempUser = new User(nickName);
        return ServerCommunication.createUser(sessionToken, tempUser);
    }

    public void setSessionCode(String code) {
        idField.setText(code);
    }

    /**
     * Switch scene to create session scene.
     *
     * @throws IOException when file could not be found
     */
    public void createSession() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateSessionScene.fxml"));
        Stage stage = (Stage) (joinSessionButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    /**
     * Switch scene to how to create session scene.
     *
     * @throws IOException when file could not be found
     */
    public void howToJoinSession() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/HowToJoinSessionScene.fxml"));
        Stage stage = (Stage) (joinSessionButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    /**
     * Switch scene to how to student view scene.
     *
     * @throws IOException when file could not be found
     */
    public void studentView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/StudentViewScene.fxml"));
        Stage stage = (Stage) (joinSessionButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.setMaximized(true);
        });
    }

    /**
     * Switch scene to Moderator View when power token is provided.
     * @throws IOException when file could not be found
     */
    public void moderatorView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModeratorViewScene.fxml"));
        Stage stage = (Stage) (joinSessionButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.setMaximized(true);
        });
    }

}

package nl.tudelft.oopp.project.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.project.Session;


public class SessionCodesSceneController {

    @FXML
    private JFXTextField moderatorCodeField;
    @FXML
    private JFXTextField studentCodeField;
    @FXML
    private JFXButton joinAsStudentButton;
    @FXML
    private JFXButton joinAsModeratorButton;

    private Session session;

    /**
     * Initializes both text fields and sets session data.
     * @param session the newly created session.
     */
    public void setData(Session session) {
        this.session = session;
        moderatorCodeField.setText(session.getPowerToken().toString());
        studentCodeField.setText(session.getStudentToken().toString());
    }

    public void joinAsStudent() throws IOException {
        switchToJoinScene(session.getStudentToken().toString());
    }

    public void joinAsModerator() throws IOException {
        switchToJoinScene(session.getPowerToken().toString());
    }

    private void switchToJoinScene(String sessionCode) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/JoinSessionScene.fxml"));
        Stage stage = (Stage) (joinAsStudentButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        JoinSceneController controller = loader.getController();
        controller.setSessionCode(sessionCode);
    }

    /**
     * Switch scene to create session scene.
     * @throws IOException when file could not be found
     */
    public void createAnotherLecture() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateSessionScene.fxml"));
        Stage stage = (Stage) (joinAsStudentButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }
}

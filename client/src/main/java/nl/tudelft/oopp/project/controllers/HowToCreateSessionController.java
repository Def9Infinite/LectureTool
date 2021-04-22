package nl.tudelft.oopp.project.controllers;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class HowToCreateSessionController {
    @FXML
    private JFXButton goBackButton;

    /**
     * Switch scene to create session fxml.
     *
     * @throws IOException when resource cant be found
     */
    public void goBackToCreate() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateSessionScene.fxml"));
        Stage stage = (Stage) (goBackButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }
}

package nl.tudelft.oopp.project.controllers;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class HowToJoinSessionController {
    @FXML
    private JFXButton goBackButton;

    /**
     * Switch scene to Join session fxml.
     *
     * @throws IOException when resource cant be found
     */
    public void goBackToJoin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/JoinSessionScene.fxml"));
        Stage stage = (Stage) (goBackButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }
}

package nl.tudelft.oopp.project.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.tudelft.oopp.project.controllers.ModeratorViewSceneController;

public class PollCreateOption extends PollOptionCell {
    ModeratorViewSceneController controllerParent;
    JFXTextField input;
    JFXButton minusButton;
    boolean createdNextPollOption = false;

    /**
     * Init.
     * @param controllerParent creator!
     */
    public PollCreateOption(ModeratorViewSceneController controllerParent, VBox parent) {
        this.controllerParent = controllerParent;
        this.parent = parent;
        setBasicProperties();
        setInsets();
        setShadow();
        createInput();
        createMinusButton();
    }

    public String getText() {
        return input != null ? input.getText() : null;
    }

    private void createInput() {
        input = new JFXTextField();
        input.setPromptText("Answer");
        input.setFont(Font.font(13.0));
        input.setLabelFloat(true);
        input.setOnKeyPressed(event -> createNextPollOption());
        HBox.setHgrow(input, Priority.ALWAYS);
        HBox.setMargin(input, new Insets(0,12,0,0));
        this.getChildren().add(input);
    }

    private void createMinusButton() {
        minusButton = new JFXButton();
        minusButton.setText("-");
        minusButton.setFont(Font.font("default", FontWeight.BOLD, 16));
        minusButton.setStyle("-fx-background-color: #e3e3e3;");
        minusButton.setOnMouseClicked(event -> removeMe());
        this.getChildren().add(minusButton);
    }

    private void removeMe() {
        parent.getChildren().remove(this);
    }

    private void createNextPollOption() {
        if (!createdNextPollOption && parent.getChildren().size() < 10) {
            controllerParent.addPollOption();
            createdNextPollOption = true;
        }
    }

}

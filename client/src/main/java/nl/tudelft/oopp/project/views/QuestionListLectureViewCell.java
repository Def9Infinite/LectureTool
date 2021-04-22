package nl.tudelft.oopp.project.views;

import com.jfoenix.controls.JFXToggleNode;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import nl.tudelft.oopp.project.Question;
import nl.tudelft.oopp.project.User;
import nl.tudelft.oopp.project.exceptions.NoExistingUserInstance;


public class QuestionListLectureViewCell extends ListCell<Question> {

    @FXML
    private Label numUpvotes;
    @FXML
    private Label questionText;
    @FXML
    private Label userAndTime;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Pane textPane;

    private User user;

    /**
     * Load fxml file.
     */
    public QuestionListLectureViewCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/QuestionCellLecturer.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            user = User.getInstance();
        } catch (NoExistingUserInstance noExistingUserInstance) {
            noExistingUserInstance.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Question question, boolean empty) {
        super.updateItem(question,empty);
        if (empty || question == null || question.getUser() == null) {
            setGraphic(null);
            setText(null);
        } else {
            setGraphic(anchor);
            numUpvotes.setText(String.valueOf(question.getUpvoteCount()));
            questionText.setText(question.getText());
            userAndTime.setText("Asked by: " + question.getUser().getNickname());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        questionText.setPrefWidth(getListView().getWidth() - 190);
        questionText.setMaxWidth(getListView().getWidth() - 190);
    }


}

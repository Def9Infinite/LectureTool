package nl.tudelft.oopp.project.views;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXToggleNode;

import java.io.IOException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nl.tudelft.oopp.project.Question;
import nl.tudelft.oopp.project.Session;
import nl.tudelft.oopp.project.User;
import nl.tudelft.oopp.project.communication.ServerCommunication;
import nl.tudelft.oopp.project.controllers.ModeratorViewSceneController;
import nl.tudelft.oopp.project.controllers.StudentViewSceneController;
import nl.tudelft.oopp.project.exceptions.NoExistingSessionInstance;
import nl.tudelft.oopp.project.exceptions.NoExistingUserInstance;


public class QuestionListModeratorViewCell extends ListCell<Question> {

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
    @FXML
    private JFXToggleNode markAnswerButton;
    @FXML
    private JFXToggleNode upvoteButton;
    @FXML
    private JFXTextArea answerTextArea;

    private ModeratorViewSceneController parent;
    private User user;
    private Question question;
    boolean editMode = false;

    /**
     * Load fxml file.
     */
    public QuestionListModeratorViewCell(ModeratorViewSceneController parent) {
        try {
            this.parent = parent;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/QuestionCellModerator.fxml"));
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
            this.question = question;
            setGraphic(anchor);
            numUpvotes.setText(String.valueOf(question.getUpvoteCount()));
            questionText.setText(question.getText());
            userAndTime.setText("Asked by: " + question.getUser().getNickname());
            upvoteButton.setSelected(question.isUpvotedByUser(user));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        questionText.setPrefWidth(getListView().getWidth() - 190);
        questionText.setMaxWidth(getListView().getWidth() - 190);
    }

    /**
     * to edit a question.
     */
    public void editQuestion() {
        editMode = !editMode;
        answerTextArea.setText(questionText.getText());
        questionText.setText("Editing question. Press send button to save... ");
    }

    /**
     * mark a question as answered.
     */
    public void markAnswer() {
        ServerCommunication.changeStatus(getItem(), markAnswerButton.isSelected());
    }

    /**
     * To ban a user.
     */
    public void banUser() {
        ServerCommunication.banUser(question.getUser());
    }

    /**
     * To delete a question.
     */
    public void deleteQuestion() {
        ServerCommunication.deleteQuestion(getItem());
        ModeratorViewSceneController.changeQuestions();
    }

    /**
     * If not upvoted yet, add 1 upvote locally and send upvote to server
     * else remove 1 upvote locally and send remove request for upvote to the server.
     */
    public void upvote() {
        Question question = getItem();

        if (upvoteButton.isSelected()) {
            //send upvote to server
            ServerCommunication.createUpvote(question).thenAcceptAsync(u -> {
                ModeratorViewSceneController.changeQuestions();
            });

        } else {
            //remove upvote from server
            ServerCommunication.deleteUpvote(question).thenAcceptAsync(u -> {
                ModeratorViewSceneController.changeQuestions();
            });
        }
    }

    /**
     * Sends written answer to server and marks it as updated or
     * changes question text if in edit mode.
     */
    public void answerQuestion() {
        if (editMode) {
            ServerCommunication.editQuestion(getItem(), answerTextArea.getText());
            questionText.setText(answerTextArea.getText());
            answerTextArea.clear();
            Platform.runLater(() -> parent.showSnackbar("Question edited successfully."));
            editMode = !editMode;
        } else {
            if (answerTextArea.getText() == null || answerTextArea.getText().equals("")) {
                Platform.runLater(() -> parent.showSnackbar("Please write out an answer first."));
                return;
            }
            Question question = getItem();
            question.setAnswer(answerTextArea.getText());
            ServerCommunication.updateQuestion(question);
            Platform.runLater(() -> parent.showSnackbar("Question answered!"));
        }
    }
}

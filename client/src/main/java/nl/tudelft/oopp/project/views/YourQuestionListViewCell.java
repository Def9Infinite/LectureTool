package nl.tudelft.oopp.project.views;

import com.jfoenix.controls.JFXToggleNode;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import nl.tudelft.oopp.project.Question;
import nl.tudelft.oopp.project.User;
import nl.tudelft.oopp.project.communication.ServerCommunication;
import nl.tudelft.oopp.project.controllers.StudentViewSceneController;
import nl.tudelft.oopp.project.exceptions.NoExistingUserInstance;




public class YourQuestionListViewCell extends ListCell<Question> {

    @FXML
    private JFXToggleNode upvoteButton;
    @FXML
    private Label numUpvotes;
    @FXML
    private Label questionText;
    @FXML
    private Label userAndTime;
    @FXML
    private AnchorPane anchor;
    @FXML
    private ImageView deleteButton;
    @FXML
    private JFXToggleNode markAnswerButton;

    private User user;

    /**
     * Initiliazes your question list view cell.
     */
    public YourQuestionListViewCell() {
        loadFxml();
        try {
            user = User.getInstance();
        } catch (NoExistingUserInstance noExistingUserInstance) {
            noExistingUserInstance.printStackTrace();
        }
    }

    /**
     * Load the fxml of the cell.
     */
    public void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/YourQuestionCellStudent.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
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
            questionText.setPrefWidth(getListView().getWidth() - 120);
            questionText.setMaxWidth(getListView().getWidth() - 120);

            userAndTime.setText("Asked by: " + question.getUser().getNickname());
            deleteButton.setVisible(true);
            deleteButton.setDisable(false);
            upvoteButton.setSelected(question.isUpvotedByUser(user));
            markAnswerButton.setSelected(question.isAnswered());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
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
                StudentViewSceneController.changeQuestions();
            });

        } else {
            //remove upvote from server
            ServerCommunication.deleteUpvote(question).thenAcceptAsync(u -> {
                StudentViewSceneController.changeQuestions();
            });
        }
    }

    /**
     * Delete the item and refresh questions.
     */
    public void deleteQuestion() {
        ServerCommunication.deleteQuestion(getItem()).thenAcceptAsync(
            question -> StudentViewSceneController.changeQuestions());
    }

    /**
     * Mark question as answered.
     */
    public void markAnswer() {
        ServerCommunication.changeStatus(getItem(), markAnswerButton.isSelected());
    }
}

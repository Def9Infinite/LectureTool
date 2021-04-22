package nl.tudelft.oopp.project.views;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import nl.tudelft.oopp.project.Poll;
import nl.tudelft.oopp.project.User;
import nl.tudelft.oopp.project.communication.ServerCommunication;
import nl.tudelft.oopp.project.exceptions.NoExistingUserInstance;

public class PollVoteOption extends PollOptionCell {
    Poll poll;
    Poll.PollOption pollOption;
    boolean selected = false;

    PollVoteOption(VBox parent, Poll poll, Poll.PollOption pollOption) {
        this.parent = parent;
        this.poll = poll;
        this.pollOption = pollOption;
        addRipple();
        setBasicProperties();
        setInsets();
        setShadow();
        addOptionLabel();
        rippler.setOnMouseClicked(event -> setVote());
        initVote();
    }

    private void initVote() {
        try {
            if (User.getInstance().getPollResponseIds().contains(pollOption.getId())) {
                innerContainer.setStyle("-fx-background-color: #8FE7FF;");
            }
        } catch (NoExistingUserInstance noExistingUserInstance) {
            noExistingUserInstance.printStackTrace();
        }
    }

    private void addOptionLabel() {
        Label optionText = new Label(pollOption.getText());
        optionText.setFont(Font.font(14));
        innerContainer.getChildren().add(optionText);
    }

    private void setVote() {
        innerContainer.setStyle("-fx-background-color: #8FE7FF;");
        parent.getChildren().stream()
                .filter(c -> c != this && c instanceof PollVoteOption)
                .forEach(c -> ((PollVoteOption) c).removeVote());
        ServerCommunication.setVote(poll, pollOption);
        try {
            User.getInstance().getPollResponseIds().add(pollOption.getId());
        } catch (NoExistingUserInstance noExistingUserInstance) {
            noExistingUserInstance.printStackTrace();
        }
    }

    private void removeVote() {
        innerContainer.setStyle("-fx-background-color: #FFFFFF;");
        try {
            User.getInstance().getPollResponseIds().remove(pollOption.getId());
        } catch (NoExistingUserInstance noExistingUserInstance) {
            noExistingUserInstance.printStackTrace();
        }
    }
}

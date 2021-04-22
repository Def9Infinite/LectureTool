package nl.tudelft.oopp.project.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import nl.tudelft.oopp.project.Poll;

public class PollResultOption extends PollOptionCell {
    Poll poll;
    Poll.PollOption pollOption;
    StackPane stackPane;

    PollResultOption(VBox parent, Poll poll, Poll.PollOption pollOption) {
        this.parent = parent;
        this.poll = poll;
        this.pollOption = pollOption;
        addRipple();
        setBasicProperties();
        setShadow();
        createStackPane();
        setInsets();
        createResultOverlay();
        addResultText();
    }

    @Override
    protected void setInsets() {
        VBox.setMargin(this, new Insets(4, 4, 4, 4));
    }

    private void createStackPane() {
        stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER_LEFT);
        innerContainer.getChildren().add(stackPane);
    }

    private void createResultOverlay() {
        double ratio = pollOption.getVoteCount() / (double) poll.getTotalVotes();
        Rectangle rect = new Rectangle(200, 20);
        rect.widthProperty().bind(this.widthProperty().multiply(ratio));
        rect.heightProperty().bind(innerContainer.heightProperty());
        rect.setStyle("-fx-fill: #8FE7FF;");
        stackPane.getChildren().add(rect);
    }

    private void addResultText() {
        HBox resultTextBox = new HBox();
        resultTextBox.setPadding(new Insets(12,12,10,12));
        resultTextBox.setAlignment(Pos.BASELINE_LEFT);
        stackPane.getChildren().add(resultTextBox);

        Label optionText = new Label(pollOption.getText());
        optionText.setFont(Font.font(14));
        optionText.setWrapText(true);
        resultTextBox.getChildren().add(optionText);

        Label resultText;
        if (pollOption.getVoteCount() == 1) {
            resultText = new Label(" (1 vote)");
        } else {
            resultText = new Label(" (" + pollOption.getVoteCount() + " votes)");
        }
        resultText.setFont(Font.font("default", FontPosture.ITALIC, 12));
        resultText.setMinWidth(90);
        resultTextBox.getChildren().add(resultText);
    }
}

package nl.tudelft.oopp.project.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTabPane;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.tudelft.oopp.project.Poll;
import nl.tudelft.oopp.project.communication.ServerCommunication;

public class PollTab extends Tab {
    JFXTabPane parent;
    Poll poll;
    int pollIndex;
    VBox container;
    Label questionText;
    boolean isModView;

    /**
     * Creates new poll tab.
     * @param parent Tabpane in which it exists
     * @param poll Poll for which it is created
     * @param pollIndex Local index of poll
     * @param isModView Are we in mod view?
     */
    public PollTab(JFXTabPane parent, Poll poll, int pollIndex, boolean isModView) {
        this.parent = parent;
        this.poll = poll;
        this.pollIndex = pollIndex;
        this.isModView = isModView;
        setBasicLayout();
        if (poll.getOpen() && !isModView) {
            setOpenLayout();
        } else {
            setResultLayout();
        }
        addToggleButton(!isModView || !poll.getOpen());
    }

    /**
     * Update information is this tab.
     * @param updatedPoll new base poll.
     */
    public void updatePoll(Poll updatedPoll) {
        poll = updatedPoll;
        setBasicLayout();
        if (isModView || !updatedPoll.getOpen()) {
            setResultLayout();
        } else {
            setOpenLayout();
        }
        addToggleButton(!isModView || !updatedPoll.getOpen());
    }

    /**
     * Handles setting the basic propertie of this tab.
     */
    public void setBasicLayout() {
        if (poll.getOpen()) {
            this.setText("Poll " + pollIndex);
        } else {
            this.setText("Poll " + pollIndex + " (closed)");
        }
        ScrollPane scrollPane = new ScrollPane();
        container = new VBox();
        scrollPane.setContent(container);
        scrollPane.setFitToWidth(true);
        this.setContent(scrollPane);
        questionText = new Label(poll.getQuestion());
        questionText.setFont(Font.font("default", FontWeight.BOLD, 15));
        questionText.setWrapText(true);
        VBox.setMargin(questionText, new Insets(8,8,8,8));
        container.getChildren().add(questionText);
    }

    /**
     * Create list of clickable poll options.
     */
    public void setOpenLayout() {
        for (Poll.PollOption pollOption : poll.getPollOptions()) {
            PollVoteOption pvo = new PollVoteOption(container, poll, pollOption);
            container.getChildren().add(pvo);
        }
    }

    /**
     * Creates list of result options.
     */
    public void setResultLayout() {
        for (Poll.PollOption pollOption : poll.getPollOptions()) {
            PollResultOption pro = new PollResultOption(container, poll, pollOption);
            container.getChildren().add(pro);
        }
    }

    /**
     * Adds a close poll button to tab.
     */
    public void addToggleButton(boolean action) {
        if (!isModView) {
            return;
        }
        JFXButton closeButton;
        if (action) {
            closeButton = new JFXButton("Open poll");
            closeButton.setOnMouseClicked(event -> ServerCommunication.reopenPoll(poll));
        } else {
            closeButton = new JFXButton("Close poll");
            closeButton.setOnMouseClicked(event -> ServerCommunication.closePoll(poll));
        }
        closeButton.setStyle("-fx-background-color:  #00A6D6;");
        closeButton.setTextFill(Color.WHITE);
        closeButton.setFont(Font.font(15));
        VBox.setMargin(closeButton, new Insets(8));
        container.getChildren().add(closeButton);
    }

    public Poll getPoll() {
        return poll;
    }
}

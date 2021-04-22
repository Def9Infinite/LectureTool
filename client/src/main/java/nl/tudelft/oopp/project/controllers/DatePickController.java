package nl.tudelft.oopp.project.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nl.tudelft.oopp.project.Session;
import nl.tudelft.oopp.project.communication.ServerCommunication;



public class DatePickController {

    @FXML
    JFXButton createSessionButton;
    @FXML
    JFXDatePicker date;
    @FXML
    JFXTimePicker timepick;
    @FXML
    JFXTextField nameField;
    @FXML
    private JFXSnackbar snackbarContainer;

    /**
     * Schedules a session, for the given date.
     */
    public void createSession() {
        if (date.getValue() != null && timepick.getValue() != null) {
            int year = date.getValue().getYear();
            int month = date.getValue().getMonthValue();
            int day = date.getValue().getDayOfMonth();
            int hour = timepick.getValue().getHour();
            int minute = timepick.getValue().getMinute();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss.SSS");
            String dateString = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00.000";
            try {
                Date date = sdf.parse(dateString);
                if (date.compareTo(new Date(System.currentTimeMillis())) > 0) {
                    ServerCommunication.scheduleSession(nameField.getText(), dateString).thenAccept(session -> {
                        try {
                            switchToSessionCodesScene(session);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    showSnackbar("Can't schedule a session in the past");
                }

            } catch (Exception e) {
                showSnackbar("Please enter a valid date + time");
            }


        } else {
            showSnackbar("Please enter a valid date + time");
        }

    }

    private void switchToSessionCodesScene(Session session) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SessionCodesScene.fxml"));
        Stage stage = (Stage) (createSessionButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        Platform.runLater(() -> {
            SessionCodesSceneController controller = loader.getController();
            stage.setScene(scene);
            controller.setData(session);
        });
    }

    public TextField getNameField() {
        return nameField;
    }

    /**
     * Show a snackbar to let users know it worked.
     * @param text text that will be shown
     */
    public void showSnackbar(String text) {
        Label test = new Label(text);
        test.setPadding(new Insets(0,20,0,20));
        test.getStyleClass().add("snackbar-label");
        snackbarContainer.enqueue(new JFXSnackbar.SnackbarEvent(test));
    }

    /**
     * Switch scene to create lecture view.
     * @throws IOException if file cannot be found
     */
    public void switchCreate() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateSessionScene.fxml"));
        Stage stage = (Stage) (createSessionButton).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        Platform.runLater(() -> {
            stage.setScene(scene);
        });
    }
}

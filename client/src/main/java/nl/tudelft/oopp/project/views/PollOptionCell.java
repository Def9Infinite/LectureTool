package nl.tudelft.oopp.project.views;

import com.jfoenix.controls.JFXRippler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class PollOptionCell extends HBox {
    VBox parent;
    HBox innerContainer = this;
    JFXRippler rippler;

    protected void setBasicProperties() {
        innerContainer.setAlignment(Pos.CENTER_LEFT);
        innerContainer.setMinSize(50.0, 30.0);
        innerContainer.setStyle("-fx-background-color: #FFFFFF;");
        HBox.setHgrow(innerContainer, Priority.ALWAYS);
    }

    protected void setInsets() {
        VBox.setMargin(this, new Insets(4, 4, 4, 4));
        innerContainer.setPadding(new Insets(12,12,10,12));
    }

    protected void setShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setWidth(3);
        dropShadow.setHeight(3);
        dropShadow.setOffsetX(1);
        dropShadow.setOffsetY(1);
        dropShadow.setRadius(3);
        dropShadow.setColor(Color.rgb(165,165,165));
        this.setEffect(dropShadow);
    }

    protected void addRipple() {
        innerContainer = new HBox();
        innerContainer.setAlignment(Pos.CENTER_LEFT);
        rippler = new JFXRippler(innerContainer);
        HBox.setHgrow(rippler, Priority.ALWAYS);
        this.getChildren().add(rippler);

    }
}

package fx.screens;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class AlertScreen extends Stage
{
    static AlertScreen me             = new AlertScreen();
    final VBox         displayWrapper = new VBox();

    public AlertScreen()
    {
        this.setAlwaysOnTop(true);
        this.initStyle(StageStyle.TRANSPARENT);

        displayWrapper.setSpacing(-30);
        displayWrapper.setStyle("-fx-background-color: null;");
        displayWrapper.setAlignment(Pos.CENTER);
        final Scene scene = new Scene(displayWrapper);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
    }

    public static synchronized void showAlert(final String alert)
    {

        final Label lbl = new Label(alert);
        lbl.setFont(Font.font(null, FontWeight.BOLD, 90));
        lbl.setTextFill(Color.AQUAMARINE);
        me.displayWrapper.getChildren().add(lbl);

        final FadeTransition fade = new FadeTransition(Duration.seconds(5), lbl);
        fade.setFromValue(1.0);
        fade.setToValue(0);
        fade.setOnFinished(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent event)
            {
                me.displayWrapper.getChildren().remove(lbl);
            }
        });
        fade.play();

        if (!me.isShowing())
        {
            me.show();
        }

        me.sizeToScene();
        me.centerOnScreen();

    }
}

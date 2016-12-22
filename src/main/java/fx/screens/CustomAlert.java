package fx.screens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomAlert extends Stage
{
    private final CustomAlert me;
    private final StackPane   stage;

    public CustomAlert(final String title, final String description)
    {
        super();
        me = this;

        this.initStyle(StageStyle.TRANSPARENT);
        this.setTitle(title);

        stage = new StackPane();
        stage.setStyle("-fx-background-color: rgba(20, 20, 20, 0.8); -fx-background-radius: 10;");

        final Label customTitle = new Label(title);
        customTitle.setFont(Font.font(null, FontWeight.BOLD, 16));
        customTitle.setTextFill(Color.ALICEBLUE);
        final Label txt = new Label();
        txt.setMaxWidth(500);
        txt.setFont(Font.font(null, FontWeight.BOLD, 14));
        txt.setTextFill(Color.FLORALWHITE);

        txt.setWrapText(true);
        txt.setText(description);

        final Button ok = new Button("Ok");
        final HBox btnBox = new HBox();
        btnBox.setAlignment(Pos.BOTTOM_RIGHT);
        btnBox.getChildren().add(ok);
        ok.setAlignment(Pos.BOTTOM_RIGHT);
        HBox.setHgrow(ok, Priority.ALWAYS);
        final VBox box = new VBox();
        box.setSpacing(10);
        box.setPadding(new Insets(10));
        box.getChildren().addAll(customTitle, txt, btnBox);
        stage.getChildren().add(box);

        ok.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(final ActionEvent event)
            {
                me.close();
            }
        });

        final Scene scene = new Scene(stage);
        scene.setFill(Color.TRANSPARENT);
        this.setAlwaysOnTop(true);
        this.setScene(scene);
    }
}

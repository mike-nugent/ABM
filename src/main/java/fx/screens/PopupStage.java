package fx.screens;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopupStage extends Stage
{
    public StackPane stage;
    public Stage     me;

    public PopupStage(final String title)
    {
        super();
        me = this;

        this.setWidth(600);
        this.setHeight(600);
        this.setAlwaysOnTop(true);

        this.initStyle(StageStyle.UTILITY);
        this.setTitle(title);

        stage = new StackPane();

        final Scene scene = new Scene(stage, 500, 500);
        this.setScene(scene);
    }
}

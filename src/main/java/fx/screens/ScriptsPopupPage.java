package fx.screens;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ScriptsPopupPage extends PopupStage
{
    TabPane pane = new TabPane();

    public ScriptsPopupPage()
    {
        super("Custom ASDM Scripts");

        final Tab scriptsPane = new Tab("Scripts");
        final Tab soundsPane = new Tab("Sounds");
        final Tab alertsPane = new Tab("Alert Options");

        scriptsPane.setClosable(false);
        soundsPane.setClosable(false);
        alertsPane.setClosable(false);

        scriptsPane.setStyle("-fx-font-size: 18px; ");
        soundsPane.setStyle("-fx-font-size: 18px; ");
        alertsPane.setStyle("-fx-font-size: 18px; ");

        pane.getTabs().addAll(scriptsPane, soundsPane, alertsPane);

        this.setWidth(1200);

        scriptsPane.setContent(new ScriptsScreen());
        soundsPane.setContent(new SoundsScreen());

        stage.getChildren().add(pane);
    }
}

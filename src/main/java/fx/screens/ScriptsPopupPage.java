package fx.screens;

import java.util.Optional;

import gameinfo.ScriptData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class ScriptsPopupPage extends PopupStage
{
    static ScriptsPopupPage instance;
    TabPane                 pane          = new TabPane();
    private final VBox      actualScripts = new VBox();
    private final VBox      scriptCreator = new VBox();

    final Button addNewBtn = new Button("Create New Script");

    public ScriptsPopupPage()
    {
        super("Custom ASDM Scripts");

        instance = this;
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

        addNewBtn.setStyle("-fx-font-size: 18px; ");

        final ScrollPane p = new ScrollPane();
        final VBox spView = new VBox();
        spView.setSpacing(10);
        spView.setPadding(new Insets(10, 10, 10, 10));
        spView.setAlignment(Pos.CENTER_LEFT);

        actualScripts.setAlignment(Pos.CENTER_LEFT);
        scriptCreator.setAlignment(Pos.CENTER_LEFT);

        actualScripts.setSpacing(10);
        actualScripts.setPadding(new Insets(10, 10, 10, 10));
        populateScriptsFromDatabase();

        scriptCreator.setSpacing(10);
        scriptCreator.setPadding(new Insets(10, 10, 10, 10));

        spView.getChildren().addAll(actualScripts, scriptCreator, addNewBtn);
        p.setContent(spView);

        addNewBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                scriptCreator.getChildren().add(new ScriptBarCreator());
                addNewBtn.setDisable(true);
            }
        });

        scriptsPane.setContent(p);

        stage.getChildren().add(pane);
    }

    private void populateScriptsFromDatabase()
    {
        ScriptsUpdater.populateScriptsFromDatabase();

    }

    public static void deleteScriptCreator(final ScriptBarCreator bar)
    {
        // final Alert alert = new Alert(AlertType.CONFIRMATION);
        // //alert.setTitle("Confirm Delete Script");
        // alert.setHeaderText("Delete this script?");
        // alert.setContentText("T");

        // final Optional<ButtonType> result = alert.showAndWait();
        // if (result.get() == ButtonType.OK)
        // {
        instance.scriptCreator.getChildren().remove(bar);
        instance.addNewBtn.setDisable(false);
        // }
    }

    public static void createScript(final ScriptData returnData, final ScriptBarCreator local)
    {
        instance.actualScripts.getChildren().add(new ScriptBar(returnData));
        if (local != null)
        {
            deleteScriptCreator(local);
        }
    }

    public static void deleteScript(final ScriptBar bar)
    {
        final Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Script");
        alert.setHeaderText("Delete this script?");
        alert.setContentText("script = " + bar.textField.getText());

        final Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            instance.actualScripts.getChildren().remove(bar);
        }
    }
}

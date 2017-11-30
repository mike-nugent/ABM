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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ScriptsScreen extends HBox
{
    private final VBox   actualScripts = new VBox();
    private final VBox   scriptCreator = new VBox();
    static ScriptsScreen instance;
    final Button         addNewBtn     = new Button("Create New Script");

    public ScriptsScreen()
    {
        instance = this;
        addNewBtn.setStyle("-fx-font-size: 18px; ");

        final ScrollPane p = new ScrollPane();
        HBox.setHgrow(p, Priority.ALWAYS);
        VBox.setVgrow(p, Priority.ALWAYS);

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

        final VBox titleWrapper = new VBox();
        titleWrapper.setSpacing(10);
        titleWrapper.setPadding(new Insets(10));

        final Label title = new Label("Custom Scripts");
        title.setFont(Font.font(null, FontWeight.BOLD, 20));

        titleWrapper.getChildren().addAll(title, p);
        HBox.setHgrow(titleWrapper, Priority.ALWAYS);
        VBox.setVgrow(titleWrapper, Priority.ALWAYS);

        this.getChildren().add(titleWrapper);
    }

    private void populateScriptsFromDatabase()
    {
        ScriptsUpdater.populateScriptsFromDatabase();
    }

    public static void deleteScriptCreator(final ScriptBarCreator bar)
    {
        instance.scriptCreator.getChildren().remove(bar);
        instance.addNewBtn.setDisable(false);
    }

    public static void createScript(final ScriptData returnData, final ScriptBarCreator local)
    {
        instance.actualScripts.getChildren().add(new ScriptBar(returnData));
        if (local != null)
        {
            deleteScriptCreator(local);
        }
    }

    public static boolean deleteScript(final ScriptBar bar)
    {
        final Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Script");
        alert.setHeaderText("Delete this script?");
        alert.setContentText("script = " + bar.textField.getText());

        final Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            instance.actualScripts.getChildren().remove(bar);
            return true;
        }
        return false;
    }

    public static void editScript(final ScriptBar bar, final boolean isEdit)
    {
        int childIndex = 0;
        for (int i = 0; i < instance.actualScripts.getChildren().size(); i++)
        {
            if (instance.actualScripts.getChildren().get(i).equals(bar))
            {
                childIndex = i;
            }
        }
        instance.actualScripts.getChildren().add(childIndex, new ScriptBarCreator(isEdit, bar, childIndex));
        instance.actualScripts.getChildren().remove(bar);
    }

    public static void cancelEdit(final ScriptBar editBarRef, final int editRefIndex, final ScriptBarCreator editor)
    {
        instance.actualScripts.getChildren().add(editRefIndex, editBarRef);
        instance.actualScripts.getChildren().remove(editor);
    }
}

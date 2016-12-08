package fx.screens;

import gameinfo.IconLoader;
import gameinfo.ScriptData;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class ScriptBar extends HBox
{
    public TextField textField    = new TextField();
    ImageView        editButton   = new ImageView(IconLoader.loadFxImage("edit-icon.png", 25));
    ImageView        deleteButton = new ImageView(IconLoader.loadFxImage("close-icon.png", 25));
    Button           saveButton   = new Button("Save Changes", new ImageView(IconLoader.loadFxImage("check.png", 25)));

    private final ScriptData _data;

    ScriptBar me;

    public ScriptBar(final ScriptData data)
    {
        me = this;
        _data = data;
        textField.setText(_data.script);
        textField.setStyle("-fx-control-inner-background: #dddddd");
        textField.setPrefWidth(600);
        textField.setEditable(false);

        saveButton.setVisible(false);

        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);

        doEdit();
        doDelete();
        doSave();

        me.getChildren().addAll(editButton, textField, deleteButton);
    }

    private void doDelete()
    {
        deleteButton.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                ScriptsUpdater.deleteScript(me, _data);
            }
        });
    }

    public void setDisabledColor(final TextField tf)
    {
        tf.setEditable(false);
        tf.setStyle("-fx-control-inner-background: #dddddd");
    }

    public void setEnabledColor(final TextField tf)
    {
        tf.setEditable(true);
        tf.setStyle("-fx-control-inner-background: #ffffff");

    }

    private void doSave()
    {
        saveButton.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                // otherwise the user wants to save
                setDisabledColor(textField);
                deleteButton.setVisible(true);
                saveButton.setVisible(false);
                editButton.setVisible(true);
                me.getChildren().remove(saveButton);
                me.getChildren().add(0, editButton);

                final String newTxt = textField.getText();
                final String oldTxt = _data.script;

                if (!newTxt.equals(oldTxt))
                {
                    _data.script = textField.getText();
                    ScriptsUpdater.updateScript(_data);
                }
                else
                {
                    System.out.println("no need to modify");
                }
            }
        });
    }

    private void doEdit()
    {
        editButton.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {

                // User wants to edit. let them do this
                setEnabledColor(textField);
                deleteButton.setVisible(false);
                saveButton.setVisible(true);
                editButton.setVisible(false);
                me.getChildren().remove(editButton);
                me.getChildren().add(0, saveButton);
            }
        });
    }
}

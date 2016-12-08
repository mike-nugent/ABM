package fx.screens;

import gameinfo.IconLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ScriptBarCreator extends Pane
{
    public static int COMPONENT_SIZE = 200;

    private TextField        scriptField;
    private ComboBox<String> soundList;
    private CheckBox         alertBox;
    private TextField        customAlertField;
    private Button           closeBtn;
    private Button           saveBtn;
    private ComboBox<String> timerList;
    private CheckBox         timerCheckbox;
    private TextField        timeText;
    private CheckBox         soundCheck;

    public ScriptBarCreator()
    {
        setupLogField();
        setupSoundOptions();
        setupAlertOptions();
        setupTimerOptions();
        setupButtons();

        // Create the left box
        final VBox leftBox = new VBox();
        leftBox.setSpacing(10);
        leftBox.setPadding(new Insets(10, 10, 10, 10));
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setStyle("-fx-background-color: #dddddd;");
        leftBox.getChildren().addAll(new Label("When the following log is encountered, do the following..."),
                scriptField, wrap(saveBtn, closeBtn));

        // Create the right box
        final VBox rightBox = new VBox();
        rightBox.setSpacing(10);
        rightBox.setPadding(new Insets(10, 10, 10, 10));
        rightBox.setAlignment(Pos.CENTER_LEFT);
        rightBox.setStyle("-fx-background-color: #cccccc;");
        rightBox.getChildren().addAll(wrap(timerCheckbox, timerList, timeText), wrap(soundCheck, soundList),
                wrap(alertBox, customAlertField));

        // Create the wrapper
        final HBox wrapper = new HBox();
        wrapper.getChildren().addAll(leftBox, rightBox);
        this.getChildren().add(wrapper);
    }

    private void setupButtons()
    {
        final ScriptBarCreator local = this;
        closeBtn = new Button("Cancel", new ImageView(IconLoader.loadFxImage("close.png", 20)));
        saveBtn = new Button("Create & Apply", new ImageView(IconLoader.loadFxImage("save.png", 20)));

        closeBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                ScriptsUpdater.deleteScript(local);
            }
        });

        saveBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                String completedScript = "";
                completedScript += "WHEN [" + scriptField.getText() + "], ";
                if (timerCheckbox.isSelected())
                {
                    completedScript += "START [" + timerList.getSelectionModel().getSelectedItem() + "], ";
                }
                if (alertBox.isSelected())
                {
                    completedScript += "SHOW [" + customAlertField.getText() + "], ";
                }
                if (soundCheck.isSelected())
                {
                    completedScript += "PLAY [" + soundList.getSelectionModel().getSelectedItem() + "]";
                }

                ScriptsUpdater.createScript(completedScript, local);

            }
        });

    }

    private void setupTimerOptions()
    {
        timerCheckbox = new CheckBox("Start Timer");

        timerCheckbox.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                if (timerCheckbox.isSelected())
                {
                    timerList.setVisible(true);
                    timeText.setVisible(true);

                }
                else
                {
                    timerList.setVisible(false);
                    timeText.setVisible(false);

                }
            }
        });
        final ObservableList<String> timerOptions = FXCollections.observableArrayList("Count Down From", "Count Up To");

        timerList = new ComboBox<String>(timerOptions);
        timerList.setPrefWidth(COMPONENT_SIZE);
        timerList.getSelectionModel().select("Count Down From");
        timerList.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                System.out.println("Time selected: " + timerList.getValue());
            }
        });

        timeText = new TextField();
        timeText.setPrefWidth(COMPONENT_SIZE);
        timerList.setVisible(false);
        timeText.setVisible(false);
    }

    private void setupAlertOptions()
    {
        alertBox = new CheckBox("Show Alert");
        alertBox.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                if (alertBox.isSelected())
                {
                    customAlertField.setVisible(true);
                }
                else
                {
                    customAlertField.setVisible(false);
                }
            }
        });

        customAlertField = new TextField();
        customAlertField.setPrefWidth(COMPONENT_SIZE);
        customAlertField.setText("<custom alert text here>");
        customAlertField.setVisible(false);
    }

    private void setupSoundOptions()
    {
        soundCheck = new CheckBox("Play Sound");
        soundCheck.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                if (soundCheck.isSelected())
                {
                    soundList.setVisible(true);
                }
                else
                {
                    soundList.setVisible(false);
                }
            }
        });

        final ObservableList<String> classOptions = FXCollections.observableArrayList("<none>", "soundA", "soundB",
                "soundC", "soundD");
        soundList = new ComboBox<String>(classOptions);
        soundList.setPrefWidth(COMPONENT_SIZE);
        soundList.setVisible(false);
        soundList.getSelectionModel().select("<none>");
        soundList.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                System.out.println("Sound selected: " + soundList.getValue());
            }
        });
    }

    private void setupLogField()
    {
        scriptField = new TextField();
        scriptField.setPrefWidth(300);
    }

    public HBox wrap(final Node... children)
    {
        final HBox box = new HBox();
        box.setSpacing(10);
        box.setPadding(new Insets(2, 10, 2, 10));
        box.setAlignment(Pos.BASELINE_LEFT);
        box.setStyle("-fx-background-color: #dddddd;");
        box.getChildren().addAll(children);

        return box;
    }
}

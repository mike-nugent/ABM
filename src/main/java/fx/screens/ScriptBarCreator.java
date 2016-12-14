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
import sounds.SoundManager;

public class ScriptBarCreator extends Pane
{
    public static int COMPONENT_SIZE = 100;

    private final TextField        scriptField      = new TextField();
    private final ComboBox<String> soundList        = new ComboBox<String>();
    private final CheckBox         soundCheck       = new CheckBox("Play Sound");
    private final ComboBox<String> timerList        = new ComboBox<String>();
    private final ComboBox<String> timeChoice       = new ComboBox<String>();
    private final CheckBox         timerCheckbox    = new CheckBox("Start Timer");
    private final CheckBox         alertBox         = new CheckBox("Show Alert");
    private final TextField        customAlertField = new TextField();

    private final Button closeBtn     = new Button("Cancel", new ImageView(IconLoader.loadFxImage("close.png", 20)));
    private final Button saveBtn      = new Button("Create", new ImageView(IconLoader.loadFxImage("save.png", 20)));
    private final Button soundTestBtn = new Button("", new ImageView(IconLoader.loadFxImage("play-icon.png", 20)));

    public ScriptBarCreator()
    {
        setupLogField();
        setupSoundOptions();
        setupAlertOptions();
        setupTimerOptions();
        setupButtons();

        final Label leftLabel = new Label("When the following log is encountered...");
        final Label rightLabel = new Label("...do the following");

        // Create the left box
        final VBox leftBox = new VBox();
        leftBox.setSpacing(10);
        leftBox.setPadding(new Insets(10, 10, 10, 10));
        leftBox.setAlignment(Pos.TOP_LEFT);
        leftBox.setStyle("-fx-background-color: #dddddd;");

        leftBox.getChildren().addAll(leftLabel, scriptField, wrap(saveBtn, closeBtn));

        // Create the right box
        final VBox rightBox = new VBox();
        rightBox.setSpacing(10);
        rightBox.setPadding(new Insets(10, 10, 10, 10));
        rightBox.setAlignment(Pos.TOP_LEFT);
        rightBox.setStyle("-fx-background-color: #cccccc;");
        rightBox.getChildren().addAll(rightLabel, wrap(timerCheckbox, timerList, timeChoice),
                wrap(soundCheck, soundList, soundTestBtn), wrap(alertBox, customAlertField));

        // Create the wrapper
        final HBox wrapper = new HBox();
        wrapper.getChildren().addAll(leftBox, rightBox);
        this.getChildren().add(wrapper);
    }

    private void setupButtons()
    {
        final ScriptBarCreator local = this;

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
                    completedScript += "START [" + timerList.getSelectionModel().getSelectedItem() + ","
                            + timeChoice.getSelectionModel().getSelectedItem() + "], ";
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

        timerCheckbox.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                if (timerCheckbox.isSelected())
                {
                    timerList.setVisible(true);
                    timeChoice.setVisible(true);

                }
                else
                {
                    timerList.setVisible(false);
                    timeChoice.setVisible(false);

                }
            }
        });
        final ObservableList<String> timerOptions = FXCollections.observableArrayList("Count Down From", "Count Up To");

        timerList.setItems(timerOptions);
        timerList.setPrefWidth(150);
        timerList.getSelectionModel().select("Count Down From");
        timerList.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                System.out.println("Time selected: " + timerList.getValue());
            }
        });

        final ObservableList<String> timeOptions = FXCollections.observableArrayList("00:05", "00:10", "00:15", "00:30",
                "00:45", "00:55", "01:00", "01:30", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00",
                "09:00", "10:00", "20:00", "30:00", "40:00", "50:00", "60:00");
        timeChoice.setItems(timeOptions);
        timeChoice.getSelectionModel().select("05:00");
        timeChoice.setPrefWidth(75);
        timerList.setVisible(false);
        timeChoice.setVisible(false);
    }

    private void setupAlertOptions()
    {
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

        customAlertField.setPrefWidth(250);
        customAlertField.setText("<custom alert text here>");
        customAlertField.setVisible(false);
    }

    private void setupSoundOptions()
    {

        soundTestBtn.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(final ActionEvent event)
            {
                if (!soundList.getValue().equals("<none>"))
                {
                    SoundManager.playEmbeddedSound(soundList.getValue());
                }

            }
        });
        soundCheck.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                if (soundCheck.isSelected())
                {
                    soundList.setVisible(true);
                    if (!soundList.getValue().equals("<none>"))
                    {
                        soundTestBtn.setVisible(true);
                    }
                }
                else
                {
                    soundList.setVisible(false);
                    soundTestBtn.setVisible(false);
                }
            }
        });

        final ObservableList<String> classOptions = FXCollections.observableArrayList(SoundManager.getEmbeddedSounds());
        soundList.setItems(classOptions);
        soundList.setPrefWidth(200);
        soundList.setVisible(false);
        soundTestBtn.setVisible(false);
        soundTestBtn.setPrefWidth(40);
        soundList.getSelectionModel().select("<none>");
        soundList.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                if (!soundTestBtn.isVisible())
                {
                    soundTestBtn.setVisible(true);
                }
                // undManager.playEmbeddedSound(soundList.getValue());
            }
        });
    }

    private void setupLogField()
    {
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

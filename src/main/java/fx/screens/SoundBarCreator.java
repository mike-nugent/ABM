package fx.screens;

import java.io.File;

import gameinfo.IconLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import sounds.SoundManager;
import triage.ExceptionHandler;

public class SoundBarCreator extends HBox
{
    final TextField soundName    = new TextField();
    final TextField pathLocation = new TextField();
    final Button    saveAndApply = new Button("Save and Apply", new ImageView(IconLoader.loadFxImage("save.png", 20)));

    public SoundBarCreator()
    {
        this.setPadding(new Insets(20));
        soundName.setPrefWidth(220);
        pathLocation.setPrefWidth(180);

        final VBox vbox = new VBox();
        final Label name = new Label("Custom Name: ");
        final Label path = new Label("Location: ");

        name.setPrefWidth(120);
        path.setPrefWidth(120);

        name.setFont(Font.font(null, FontWeight.BOLD, 15));
        path.setFont(Font.font(null, FontWeight.BOLD, 15));

        final Button chooseBtn = new Button("", new ImageView(IconLoader.loadFxImage("config.png", 15)));

        saveAndApply.setDisable(true);

        final HBox top = new HBox();
        final HBox mid = new HBox();
        final HBox bottom = new HBox();

        top.setAlignment(Pos.CENTER_LEFT);
        mid.setAlignment(Pos.CENTER_LEFT);
        bottom.setAlignment(Pos.CENTER_LEFT);

        top.setPadding(new Insets(5));
        mid.setPadding(new Insets(5));
        bottom.setPadding(new Insets(5));

        bottom.setPadding(new Insets(5));

        top.setSpacing(10);
        top.getChildren().addAll(name, soundName);

        mid.setSpacing(10);
        mid.getChildren().addAll(path, pathLocation, chooseBtn);

        bottom.setSpacing(10);
        final Label supportedFiles = new Label("Supported files: .wav .mp3");
        supportedFiles.setFont(Font.font(null, FontWeight.BOLD, 12));

        final Label importNewLabel = new Label("Import New Sound:");
        importNewLabel.setFont(Font.font(null, FontWeight.BOLD, 18));

        bottom.getChildren().addAll(saveAndApply, supportedFiles);
        vbox.getChildren().addAll(importNewLabel, top, mid, bottom);
        vbox.setPadding(new Insets(20, 0, 0, 0));
        this.getChildren().add(vbox);

        soundName.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue,
                    final String newValue)
            {
                checkReadyToSave();
            }
        });

        chooseBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent event)
            {
                final FileChooser fileChooser = new FileChooser();
                final ExtensionFilter extFilter = new ExtensionFilter("Sound File (*.wav, *.mp3)", "*.wav", "*.mp3");
                fileChooser.getExtensionFilters().add(extFilter);
                fileChooser.setTitle("Locate sound...");
                final File file = fileChooser.showOpenDialog(null);
                if (file != null)
                {
                    try
                    {
                        if (file.getAbsolutePath().endsWith(".wav") || file.getAbsolutePath().endsWith(".mp3"))
                        {
                            final String path = file.getAbsolutePath();
                            pathLocation.setText(path);
                            checkReadyToSave();
                        }
                    }
                    catch (final Exception e)
                    {
                        ExceptionHandler.handleException(e);
                    }
                }
            }
        });

        saveAndApply.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent event)
            {
                SoundManager.createNewCustomSound(soundName.getText(), pathLocation.getText());
                // Then clear out the fields.

                soundName.setText("");
                pathLocation.setText("");
                checkReadyToSave();
            }
        });
    }

    public void checkReadyToSave()
    {
        if (soundName.getText() != null && soundName.getText().trim().length() > 0 && pathLocation.getText() != null
                && new File(pathLocation.getText()).exists()
                && (pathLocation.getText().endsWith(".wav") || pathLocation.getText().endsWith(".mp3")))
        {
            saveAndApply.setDisable(false);
        }
        else
        {
            saveAndApply.setDisable(true);
        }
    }
}

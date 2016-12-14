package fx.screens;

import java.io.File;

import gameinfo.IconLoader;
import gameinfo.SoundData;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sounds.SoundManager;

public class SoundBar extends VBox
{
    private final String   _filePath;
    private final String   _fileName;
    private final boolean  _isEmbedded;
    private SoundData      _data;
    private final SoundBar me;

    public SoundBar(final String fileName, final String filePath, final boolean embedded)
    {
        me = this;
        _fileName = fileName;
        _filePath = filePath;
        _isEmbedded = embedded;

        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle("-fx-background-color: #cccccc;");

        final TextField nameField = new TextField(_fileName);
        if (embedded)
        {
            final String parsedName = _filePath.substring(0, _filePath.indexOf("."));
            nameField.setText(parsedName);
        }

        nameField.setEditable(false);

        final Label pathField = new Label(_filePath);
        pathField.setFont(Font.font(null, FontWeight.BOLD, 12));
        nameField.setFont(Font.font(16));

        // Check for any kinds of erros.
        if (!new File(_filePath).exists() || (!_filePath.endsWith(".wav") && !_filePath.endsWith(".mp3")))
        {
            pathField.setTextFill(Color.DARKRED);
            pathField.setText(pathField.getText() + " - ERROR sound file missing!");
        }

        final Button sampleBtn = new Button("", new ImageView(IconLoader.loadFxImage("play-icon.png", 25)));
        sampleBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent event)
            {
                if (_isEmbedded)
                {
                    SoundManager.playEmbeddedSound(_filePath);
                }
                else
                {
                    SoundManager.playLocalSound(_data);
                }
            }
        });

        final HBox top = new HBox();
        top.setSpacing(10);
        top.setPadding(new Insets(5, 5, 2, 5));

        final HBox bottom = new HBox();
        bottom.setPadding(new Insets(0, 0, 5, 20));

        top.getChildren().addAll(nameField, sampleBtn);
        bottom.getChildren().addAll(pathField);

        if (!_isEmbedded)
        {

            // Maybe delete this, dont need it
            final Button editButton = new Button("", new ImageView(IconLoader.loadFxImage("edit-icon.png", 25)));
            editButton.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(final ActionEvent event)
                {
                    System.out.println("TODO - edit sound");
                }
            });

            final Button deleteBtn = new Button("", new ImageView(IconLoader.loadFxImage("close-icon.png", 25)));
            deleteBtn.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(final ActionEvent event)
                {
                    SoundManager.deleteCustomSound(_data, me);
                }
            });

            top.getChildren().addAll(deleteBtn);
            this.getChildren().addAll(top, bottom);
        }
        else
        {
            this.getChildren().addAll(top);
        }
    }

    public SoundBar(final String fileName, final String filePath)
    {
        this(fileName, filePath, true);
    }

    public SoundBar(final SoundData data)
    {
        this(data.name, data.path, false);
        _data = data;
    }

    public SoundData getData()
    {
        return _data;
    }

}

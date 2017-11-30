package fx.screens;

import java.util.List;
import java.util.Optional;

import gameinfo.SoundData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sounds.SoundManager;

public class SoundsScreen extends HBox
{
    final VBox          soundList = new VBox();
    static SoundsScreen instance;

    public SoundsScreen()
    {
        VBox.setVgrow(this, Priority.ALWAYS);

        instance = this;
        // Set up embedded sounds
        final ScrollPane embeddedPane = getEmbeddedSoundList();
        final ScrollPane customPane = getCustomSoundList();
        VBox.setVgrow(embeddedPane, Priority.ALWAYS);
        VBox.setVgrow(customPane, Priority.ALWAYS);

        final Label title = new Label("Embedded Sounds");
        title.setFont(Font.font(null, FontWeight.BOLD, 20));

        final Label title2 = new Label("Custom Sounds");
        title2.setFont(Font.font(null, FontWeight.BOLD, 20));

        final VBox left = new VBox(title, embeddedPane);
        final VBox right = new VBox(title2, customPane);

        left.setSpacing(10);
        left.setPadding(new Insets(10));

        right.setSpacing(10);
        right.setPadding(new Insets(10));

        VBox.setVgrow(right, Priority.ALWAYS);
        VBox.setVgrow(left, Priority.ALWAYS);

        this.getChildren().addAll(left, right);
    }

    private ScrollPane getCustomSoundList()
    {
        final VBox root = new VBox();
        soundList.setSpacing(5);
        soundList.setPadding(new Insets(10));

        final ScrollPane pane = new ScrollPane(root);
        root.setPrefWidth(600);

        pane.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setAlignment(Pos.CENTER_LEFT);
        final List<SoundData> customSounds = SoundManager.getCustomSounds();

        for (int i = 0; i < customSounds.size(); i++)
        {
            final SoundData sound = customSounds.get(i);
            final SoundBar bar = new SoundBar(sound);
            soundList.getChildren().add(bar);
        }

        final SoundBarCreator importBar = new SoundBarCreator();
        root.getChildren().addAll(soundList, importBar);

        return pane;
    }

    private ScrollPane getEmbeddedSoundList()
    {
        final VBox root = new VBox();
        root.setPadding(new Insets(100, 50, 100, 50));

        root.setSpacing(5);
        root.setPrefWidth(600);
        root.setPadding(new Insets(10));

        final ScrollPane pane = new ScrollPane(root);
        pane.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setAlignment(Pos.CENTER_LEFT);
        final List<String> embeddedSounds = SoundManager.getEmbeddedSounds();

        for (int i = 0; i < embeddedSounds.size(); i++)
        {
            final String sound = embeddedSounds.get(i);
            final HBox wrapper = new HBox();
            wrapper.setAlignment(Pos.CENTER_LEFT);
            final Label counter = new Label((i + 1) + " )\t");
            counter.setFont(Font.font(null, FontWeight.BOLD, 20));
            final SoundBar bar = new SoundBar("", sound, true);
            wrapper.getChildren().addAll(counter, bar);
            root.getChildren().add(wrapper);
        }

        return pane;
    }

    public static boolean deleteCustomSound(final SoundData data, final SoundBar bar)
    {
        final Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Sound");
        alert.setHeaderText("Delete this Sound?");
        alert.setContentText("sound = " + data.path);

        final Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            instance.soundList.getChildren().remove(bar);
            return true;
        }
        return false;
    }

    public static void newSoundAdded(final SoundData data)
    {
        instance.soundList.getChildren().add(new SoundBar(data));
    }
}

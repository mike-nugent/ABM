package main;

import java.io.File;

import config.ConfigFile;
import gameinfo.IconLoader;
import gameinfo.Race;
import gameinfo.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

public class ConfigPopupPage extends PopupStage
{

    private final double CONTROL_SIZE = 280;

    public ConfigPopupPage()
    {
        super("Configure ASDM");

        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        final Text scenetitle = new Text("Configure the data below for ASDM to work");
        grid.add(scenetitle, 0, 0, 2, 1);

        // Create the left labels =======================================
        final Label userName = new Label("The name of your characther:");
        grid.add(userName, 0, 1);
        GridPane.setHalignment(userName, HPos.RIGHT);

        final Label server = new Label("The server you play on:");
        grid.add(server, 0, 2);
        GridPane.setHalignment(server, HPos.RIGHT);

        final Label race = new Label("Your race:");
        grid.add(race, 0, 3);
        GridPane.setHalignment(race, HPos.RIGHT);

        final Label aion = new Label("Where Aion is installed:");
        grid.add(aion, 0, 4);
        GridPane.setHalignment(aion, HPos.RIGHT);

        // Create the right controls ====================================
        final TextField nameField = new TextField();
        nameField.setPrefWidth(CONTROL_SIZE);
        grid.add(nameField, 1, 1);

        final ObservableList<Server> serverOptions = FXCollections.observableArrayList(Server.getLegalValues());
        final ComboBox<Server> serverControl = new ComboBox<Server>(serverOptions);
        serverControl.setPrefWidth(CONTROL_SIZE);
        grid.add(serverControl, 1, 2);

        final ObservableList<Race> raceOptions = FXCollections.observableArrayList(Race.getLegalValues());
        final ComboBox<Race> raceControl = new ComboBox<Race>(raceOptions);
        raceControl.setPrefWidth(CONTROL_SIZE);
        grid.add(raceControl, 1, 3);

        final TextField aionLocation = new TextField();
        aionLocation.setEditable(false);
        final Button aionButton = new Button("", new ImageView(IconLoader.loadFxImage("config.png", 15)));

        final DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select the Aion directory location");
        final File defaultDirectory = new File(ConfigFile.DEFAULT_LOG_FILE_LOCATION);
        if (defaultDirectory.exists())
        {
            chooser.setInitialDirectory(defaultDirectory);
            aionLocation.setText(ConfigFile.DEFAULT_LOG_FILE_LOCATION);
        }
        else
        {
            aionLocation.setText("Could not detect Aion install location");
        }
        aionButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                final File file = chooser.showDialog(null);
                if (file != null)
                {
                    aionLocation.setText(file.getAbsolutePath());
                }
            }
        });

        final HBox aionBox = new HBox();
        aionBox.getChildren().addAll(aionLocation, aionButton);
        aionLocation.setPrefWidth(230);
        aionButton.setPrefWidth(50);
        grid.add(aionBox, 1, 4);

        final Button btn = new Button("Save Changes");
        final HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 5);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 7);

        btn.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(final ActionEvent e)
            {
                actiontarget.setText("Sign in button pressed");
            }
        });

        stage.getChildren().add(grid);
    }

}

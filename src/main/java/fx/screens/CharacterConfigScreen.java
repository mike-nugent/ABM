package fx.screens;

import config.ConfigFile;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import skins.Skins;

public class CharacterConfigScreen extends VBox
{
    private final double CONTROL_SIZE = 280;
    ConfigPopupPage      _page;

    public CharacterConfigScreen(final ConfigPopupPage configPopupPage)
    {
        _page = configPopupPage;
        this.setAlignment(Pos.CENTER);
        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        final Text scenetitle = new Text("Who is your main character in Aion?");
        scenetitle.setStyle("-fx-font-size: 20px;");

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

        initializePlayerFields(nameField, serverControl, raceControl);

        final Button btn = new Button("Save and Next");
        btn.getStylesheets().add(Skins.get("GreenBtn.css"));

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
                final String name = nameField.getText();
                final Server server = serverControl.getValue();
                final Race race = raceControl.getValue();
                ConfigFile.setPlayerProperties(name, server, race);
                actiontarget.setText("Changes have been saved!");

                _page.saveCharacterCompleted();
            }
        });
        

        this.getChildren().addAll(grid);
    }

    private void initializePlayerFields(final TextField nameField, final ComboBox<Server> serverControl,
            final ComboBox<Race> raceControl)
    {
        nameField.setText(ConfigFile.getName());
        serverControl.setValue(ConfigFile.getServer());
        raceControl.setValue(ConfigFile.getRace());

    }

}

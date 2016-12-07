package fx.screens;

import java.util.List;

import database.AionDB;
import gameinfo.Archetype;
import gameinfo.HackList;
import gameinfo.IconLoader;
import gameinfo.PlayerData;
import gameinfo.Race;
import gameinfo.Rank;
import gameinfo.Server;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SearchPlayersScreen extends VBox
{
    final Button                searchBtn   = new Button("",
            new ImageView(IconLoader.loadFxImage("search_icon.png", 30)));
    final TextField             searchFiled = new TextField();
    final TableView<PlayerData> table       = new TableView<PlayerData>();

    public SearchPlayersScreen()
    {

        // Create search fields
        final Label label = new Label("Player Database");
        label.setFont(new Font("Arial", 20));
        searchFiled.setFont(new Font("Arial", 20));
        // searchFiled.setPrefWidth(320);

        searchBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                updateSearch();
            }
        });

        searchFiled.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue,
                    final String newValue)
            {
                updateSearch();
            }
        });

        final HBox barNBtn = new HBox();
        barNBtn.setSpacing(15);
        barNBtn.getChildren().addAll(searchFiled, searchBtn);
        HBox.setHgrow(barNBtn, Priority.ALWAYS); // Give stack any extra
        barNBtn.setAlignment(Pos.CENTER_RIGHT);

        final HBox topBox = new HBox();
        topBox.setSpacing(15);
        topBox.getChildren().addAll(label, barNBtn);
        topBox.setAlignment(Pos.CENTER_LEFT);

        final HBox bottomBox = new HBox();
        bottomBox.setSpacing(15);
        // Server
        final ObservableList<String> serverOptions = FXCollections.observableArrayList("All Servers", "Kahrun",
                "Israphel", "Siel", "Tiamat", "Beritra");
        final ComboBox<String> serverControl = new ComboBox<String>(serverOptions);
        serverControl.getSelectionModel().select("All Servers");

        // Class
        final ObservableList<String> classOptions = FXCollections.observableArrayList("All Classes", "Assassin",
                "Chanter", "Cleric", "Gladiator", "Gunslinger", "Ranger", "Songweaver", "Sorcerer", "Spiritmaster",
                "Templar");
        final ComboBox<String> classControl = new ComboBox<String>(classOptions);
        classControl.getSelectionModel().select("All Classes");

        // Race
        final ObservableList<String> raceOptions = FXCollections.observableArrayList("All Races", "Asmodian", "Elyos");
        final ComboBox<String> raceControl = new ComboBox<String>(raceOptions);
        raceControl.getSelectionModel().select("All Races");

        // Rank
        final ObservableList<String> rankOptions = FXCollections.observableArrayList("All Ranks", "Non Xform",
                "5-Star Officer", "General", "Great General", "Commander", "Governor");
        final ComboBox<String> rankControl = new ComboBox<String>(rankOptions);
        rankControl.getSelectionModel().select("All Ranks");

        // Reset
        final Button resetButton = new Button("", new ImageView(IconLoader.loadFxImage("refresh-icon.png", 20)));
        resetButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                // AionDB.addOrUpdatePlayer(PlayerData.generateRandom());

                final List<PlayerData> playerList = AionDB.getAllPlayers();
                final ObservableList<PlayerData> personData = FXCollections.observableArrayList(playerList);

                table.setItems(personData);
            }
        });

        bottomBox.getChildren().addAll(serverControl, classControl, raceControl, rankControl, resetButton);

        final VBox searchVBox = new VBox();
        searchVBox.setSpacing(15);
        searchVBox.getChildren().addAll(topBox, bottomBox);
        final TableColumn<PlayerData, String> nameCol = new TableColumn<PlayerData, String>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<PlayerData, String>("name"));

        final TableColumn<PlayerData, Server> serverCol = new TableColumn<PlayerData, Server>("Server");
        serverCol.setCellValueFactory(new PropertyValueFactory<PlayerData, Server>("server"));

        final TableColumn<PlayerData, Race> raceCol = new TableColumn<PlayerData, Race>("Race");
        raceCol.setCellValueFactory(new PropertyValueFactory<PlayerData, Race>("race"));

        final TableColumn<PlayerData, Archetype> classCol = new TableColumn<PlayerData, Archetype>("Class");
        classCol.setCellValueFactory(new PropertyValueFactory<PlayerData, Archetype>("clazz"));

        final TableColumn<PlayerData, Rank> rankCol = new TableColumn<PlayerData, Rank>("Rank");
        rankCol.setCellValueFactory(new PropertyValueFactory<PlayerData, Rank>("rank"));

        final TableColumn<PlayerData, HackList> hackCol = new TableColumn<PlayerData, HackList>("Hacks");
        hackCol.setCellValueFactory(new PropertyValueFactory<PlayerData, HackList>("hacks"));

        table.getColumns().addAll(nameCol, serverCol, raceCol, classCol, rankCol, hackCol);

        final List<PlayerData> playerList = AionDB.getAllPlayers();
        final ObservableList<PlayerData> personData = FXCollections.observableArrayList(playerList);

        table.setItems(personData);

        this.setSpacing(5);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.getChildren().addAll(searchVBox, table);
    }

    public void updateSearch()
    {
        final List<PlayerData> results = AionDB.getPlayerByName(searchFiled.getText());
        final ObservableList<PlayerData> personData = FXCollections.observableArrayList(results);
        table.setItems(personData);
    }
}

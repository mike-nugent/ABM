package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

public class ConfigPopupPage extends PopupStage
{
    private final double CONTROL_SIZE = 280;
    TabPane              _tabPane     = new TabPane();
    private Label        ovrVer;
    private Label        chatVer;
    private TextField    aionLocation;

    public ConfigPopupPage()
    {
        super("Configure ASDM");
        final Tab infoTab = getChatacterSettings();
        final Tab loggingTab = getLoggingSettings();
        _tabPane.getTabs().addAll(infoTab, loggingTab);
        this.setAlwaysOnTop(false);

        stage.getChildren().add(_tabPane);
    }

    private Tab getLoggingSettings()
    {
        final Tab loggingTab = new Tab("Logging Setting");
        loggingTab.setStyle("-fx-font-size: 18px;");
        loggingTab.setClosable(false);

        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        final Text scenetitle = new Text("Your logging settings");
        grid.add(scenetitle, 0, 0, 3, 1);

        // Create the left labels =======================================
        final Label aion = new Label("Aion Install:");
        grid.add(aion, 0, 1);
        GridPane.setHalignment(aion, HPos.RIGHT);

        final Label chatLogTxt = new Label("Chat.log:");
        grid.add(chatLogTxt, 0, 2);
        GridPane.setHalignment(chatLogTxt, HPos.RIGHT);

        final Label systemOvrTxt = new Label("system.ovr:");
        grid.add(systemOvrTxt, 0, 3);
        GridPane.setHalignment(systemOvrTxt, HPos.RIGHT);

        chatVer = new Label("MISSING");
        grid.add(chatVer, 1, 2);

        ovrVer = new Label("MISSING");
        grid.add(ovrVer, 1, 3);

        aionLocation = new TextField();
        aionLocation.setEditable(false);
        final Button aionButton = new Button("", new ImageView(IconLoader.loadFxImage("config.png", 15)));

        final DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select the Aion directory location");

        initialiazeLoggingFileds(aionLocation, chooser);

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

        aionLocation.setPrefWidth(280);
        aionButton.setPrefWidth(50);
        grid.add(aionLocation, 1, 1);
        grid.add(aionButton, 2, 1);

        // Create the right controls ====================================
        final Button chatBtn = new Button();
        chatBtn.setText("Fix This");
        chatBtn.setPrefWidth(100);
        grid.add(chatBtn, 2, 2);

        final Button ovrBtn = new Button();
        ovrBtn.setText("Fix This");
        ovrBtn.setPrefWidth(100);
        grid.add(ovrBtn, 2, 3);

        loggingTab.setContent(grid);
        return loggingTab;
    }

    private void checkFiles()
    {
        final File chatFileCheck = new File(aionLocation.getText() + "/" + ConfigFile.DEFAULT_LOG_FILE_NAME);
        final File ovrFileCheck = new File(aionLocation.getText() + "/" + ConfigFile.DEFAULT_OVR_FILE_NAME);

        if (chatFileCheck.exists() && chatFileCheck.isFile() && chatFileCheck.canRead())
        {
            chatVer.setText("VERIFIED");
        }
        else
        {
            chatVer.setText("MISSING");
        }

        if (ovrFileCheck.exists() && ovrFileCheck.isFile() && ovrFileCheck.canRead())
        {
            // Check the contents, make sure logging is turned on. Should look
            // like:

            if (readFile1(ovrFileCheck).contains("g_chatlog = 1"))
            {
                ovrVer.setText("VERIFIED");
            }
            else
            {
                ovrVer.setText("NOT ENABLED");
            }
        }
        else
        {
            ovrVer.setText("MISSING");

        }
    }

    private static String readFile1(final File fin)
    {
        String ret = "";
        try
        {
            final FileInputStream fis = new FileInputStream(fin);

            // Construct BufferedReader from InputStreamReader
            final BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line = null;
            while ((line = br.readLine()) != null)
            {
                ret += line;
            }

            br.close();
        }
        catch (final Exception e)
        {
            System.out.println("Error" + e);
        }
        return ret;

    }

    private Tab getChatacterSettings()
    {
        final Tab infoTab = new Tab("Character Settings");
        infoTab.setStyle("-fx-font-size: 18px;");
        infoTab.setClosable(false);

        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        final Text scenetitle = new Text("Who is your main character in Aion?");
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
                ConfigFile.setProperties(name, server, race);

                actiontarget.setText("Changes have been saved!");
                _tabPane.getSelectionModel().select(1);
                checkFiles();

                // TransformManager.toggleConfigPopup();
            }
        });

        infoTab.setContent(grid);
        return infoTab;
    }

    private void initialiazeLoggingFileds(final TextField aionLocation, final DirectoryChooser chooser)
    {
        String aionInstallLocation = ConfigFile.getAionInstallLocation();
        if (aionInstallLocation == null)
        {
            aionInstallLocation = ConfigFile.DEFAULT_LOG_FILE_LOCATION;
        }

        final File aionFolder = new File(aionInstallLocation);
        if (aionFolder.exists())
        {
            aionLocation.setText(aionInstallLocation);
            chooser.setInitialDirectory(aionFolder);
        }
        else
        {
            final File defaultDirectory = new File(ConfigFile.DEFAULT_LOG_FILE_LOCATION);
            if (defaultDirectory.exists())
            {
                aionLocation.setText(ConfigFile.DEFAULT_LOG_FILE_LOCATION);
                chooser.setInitialDirectory(defaultDirectory);
            }
            else
            {
                aionLocation.setText("Could not detect Aion install location");
            }
        }
    }

    private void initializePlayerFields(final TextField nameField, final ComboBox<Server> serverControl,
            final ComboBox<Race> raceControl)
    {
        nameField.setText(ConfigFile.getName());
        serverControl.setValue(ConfigFile.getServer());
        raceControl.setValue(ConfigFile.getRace());

    }
}

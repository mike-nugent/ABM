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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

public class ConfigPopupPage extends PopupStage
{
    private final double CONTROL_SIZE = 280;
    TabPane _tabPane = new TabPane();

    public ConfigPopupPage()
    {
        super("Configure ASDM");
        Tab infoTab = getChatacterSettings();
        Tab loggingTab = getLoggingSettings();
        _tabPane.getTabs().addAll(infoTab, loggingTab);
        this.setAlwaysOnTop(false);
        
        stage.getChildren().add(_tabPane);
    }

	private Tab getLoggingSettings() 
	{
	    Tab loggingTab = new Tab("Logging Setting");
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
        
  
        
        
        final Label chatVer = new Label("MISSING");
        grid.add(chatVer, 1, 2);
        
        final Label ovrVer = new Label("MISSING");
        grid.add(ovrVer, 1, 3);
        
        
        
        final TextField aionLocation = new TextField();
        aionLocation.setEditable(false);
        final Button aionButton = new Button("", new ImageView(IconLoader.loadFxImage("config.png", 15)));

        final DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select the Aion directory location");
        
        initialiazeLoggingFileds( aionLocation, chooser);
        
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

		

	private Tab getChatacterSettings() 
	{
        Tab infoTab = new Tab("Character Settings");
        infoTab.setStyle("-fx-font-size: 18px;");
        infoTab.setClosable(false);
        
        

        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        final Text scenetitle = new Text("Who is your main character you play in Aion?");
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
            	String name = nameField.getText();
    			Server server = serverControl.getValue();
    			Race race = raceControl.getValue();
    			ConfigFile.setProperties(name, server, race);
    			
                actiontarget.setText("Changes have been saved!");
                _tabPane.getSelectionModel().select(1);
                
               // TransformManager.toggleConfigPopup();
            }
        });

        infoTab.setContent(grid);
        return infoTab;
	}
	
	private void initialiazeLoggingFileds(TextField aionLocation, DirectoryChooser chooser)
	{
		String aionInstallLocation = ConfigFile.getAionInstallLocation();
		if(aionInstallLocation == null)
			aionInstallLocation = ConfigFile.DEFAULT_LOG_FILE_LOCATION;
		
		File aionFolder = new File(aionInstallLocation);
		if(aionFolder.exists())
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

	private void initializePlayerFields(
			TextField nameField, 
			ComboBox<Server> serverControl, 
			ComboBox<Race> raceControl) 
	{
		nameField.setText(ConfigFile.getName());
		serverControl.setValue(ConfigFile.getServer());
		raceControl.setValue(ConfigFile.getRace());
		
	}
}

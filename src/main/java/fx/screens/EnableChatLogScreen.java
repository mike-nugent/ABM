package fx.screens;

import java.io.File;
import java.io.IOException;

import config.ConfigFile;
import config.SystemConfigFileEditor;
import gameinfo.IconLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import main.MainFX;

public class EnableChatLogScreen extends HBox
{

    private final double    CONTROL_SIZE       = 280;
    private final Label     ovrVer             = new Label("UNCHECKED");
    private final Label     chatVer            = new Label("UNCHECKED");
    private final TextField aionLocation       = new TextField();
    private final Button    chatBtn            = new Button("Fix This");
    private final Button    ovrBtn             = new Button("Fix This");
    final Button            saveAndFinalizeBtn = new Button("Save and Finalize");
    ConfigPopupPage         _page;

    public EnableChatLogScreen(final ConfigPopupPage configPopupPage)
    {
        _page = configPopupPage;
        this.setAlignment(Pos.CENTER);
        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        final Text scenetitle = new Text("Your logging settings");
        scenetitle.setStyle("-fx-font-size: 20px;");

        grid.add(scenetitle, 0, 0, 3, 1);

        // Create the left labels =======================================
        final Label aion = new Label("Aion Install:");
        grid.add(aion, 0, 1);
        GridPane.setHalignment(aion, HPos.RIGHT);

        final Label chatLogTxt = new Label("Chat.log:");
        grid.add(chatLogTxt, 0, 2);
        GridPane.setHalignment(chatLogTxt, HPos.RIGHT);

        final Label systemOvrTxt = new Label("system.cfg:");
        grid.add(systemOvrTxt, 0, 3);
        GridPane.setHalignment(systemOvrTxt, HPos.RIGHT);

        chatVer.setStyle("-fx-font-size: 20px;" + "-fx-font-weight: bold;");
        grid.add(chatVer, 1, 2);

        ovrVer.setStyle("-fx-font-size: 20px;" + "-fx-font-weight: bold;");
        grid.add(ovrVer, 1, 3);

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
                    checkFiles();
                }
            }
        });

        aionLocation.setPrefWidth(280);
        aionButton.setPrefWidth(50);
        grid.add(aionLocation, 1, 1);
        grid.add(aionButton, 2, 1);

        // Create the right controls ====================================
        chatBtn.setPrefWidth(100);
        chatBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                // Create the file if you can.

                final File chatFileToMake = new File(aionLocation.getText() + "/" + ConfigFile.DEFAULT_LOG_FILE_NAME);
                try
                {
                    if (!chatFileToMake.exists())
                    {
                        chatFileToMake.createNewFile();
                    }
                }
                catch (final IOException e1)
                {
                    final Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Did you run as Administrator?");
                    alert.setHeaderText(
                            "Oops!\nIn order to create the Chat.log file, ASDM needs to be run in Administrator Mode.");
                    alert.setContentText(
                            "To do this, close ASDM, and restart it by right clicking, selecting Run As > Administrator.  "
                                    + "That will allow ASDM to create the Chat.log file");
                    alert.showAndWait();
                }

                checkFiles();
            }
        });
        grid.add(chatBtn, 2, 2);

        ovrBtn.setPrefWidth(100);
        ovrBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                // Create the file if you can.

                try
                {
                    final File cfgFileToCreate = new File(
                            aionLocation.getText() + "/" + ConfigFile.DEFAULT_CFG_FILE_NAME);
                    SystemConfigFileEditor.enableChatLogFile(cfgFileToCreate.getAbsolutePath());
                }
                catch (final Exception e1)
                {
                    final Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Did you run as Administrator?");
                    alert.setHeaderText(
                            "Oops!\nIn order to enable the chat logging, ASDM needs to be run in Administrator Mode.");
                    alert.setContentText(
                            "To do this, close ASDM, and restart it by right clicking, selecting Run As > Administrator.  "
                                    + "That will allow ASDM to edit the system.cfg file");
                    alert.showAndWait();
                }

                checkFiles();
            }
        });
        grid.add(ovrBtn, 2, 3);

        saveAndFinalizeBtn.setMinWidth(280);
        saveAndFinalizeBtn.setMinHeight(40);
        saveAndFinalizeBtn.setStyle("-fx-font-size: 25px;");
        saveAndFinalizeBtn.setDisable(true);
        saveAndFinalizeBtn.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(final ActionEvent e)
            {
                final String aionInstallLocation = aionLocation.getText();
                ConfigFile.setAionInstallLocation(aionInstallLocation);
                checkFiles();

                MainFX.showStage();
                MainFX.jumpStartLoggers();
                _page.close();
            }
        });
        grid.add(saveAndFinalizeBtn, 1, 4);

        this.getChildren().add(grid);
    }

    public void checkFiles()
    {
        final File chatFileCheck = new File(aionLocation.getText() + "/" + ConfigFile.DEFAULT_LOG_FILE_NAME);
        final File cfgFileCheck = new File(aionLocation.getText() + "/" + ConfigFile.DEFAULT_CFG_FILE_NAME);

        if (chatFileCheck.exists() && chatFileCheck.isFile() && chatFileCheck.canRead())
        {
            chatVer.setText("VERIFIED");
            chatVer.setTextFill(Color.DARKGREEN);
            chatBtn.setVisible(false);

        }
        else
        {
            chatVer.setText("MISSING                            -->");
            chatVer.setTextFill(Color.RED);
            chatBtn.setVisible(true);
        }

        if (cfgFileCheck.exists() && cfgFileCheck.isFile() && cfgFileCheck.canRead())
        {
            // Check the contents, make sure logging is turned on. Should look
            // like:

            try
            {
                final boolean isEnabled = SystemConfigFileEditor.isConfigFileEnabled(cfgFileCheck.getAbsolutePath());
                if (isEnabled)
                {
                    ovrVer.setText("VERIFIED");
                    ovrVer.setTextFill(Color.DARKGREEN);
                    ovrBtn.setVisible(false);

                }
                else
                {
                    ovrVer.setText("NOT ENABLED                         -->");
                    ovrVer.setTextFill(Color.LIGHTCORAL);
                    ovrBtn.setVisible(true);

                }
            }
            catch (final Exception e)
            {
                System.out.println(e);
                ovrVer.setText("ERROR IN READING                         -->");
                ovrVer.setTextFill(Color.YELLOW);
                ovrBtn.setVisible(true);
            }
        }
        else
        {
            ovrVer.setText("MISSING                            -->");
            ovrVer.setTextFill(Color.RED);
            ovrBtn.setVisible(true);

        }

        if (chatVer.getText().contains("VERIFIED") && ovrVer.getText().contains("VERIFIED"))
        {
            saveAndFinalizeBtn.setDisable(false);
        }
    }

    private void initialiazeLoggingFileds(final TextField aionLocation, final DirectoryChooser chooser)
    {
        String aionInstallLocation = ConfigFile.getAionInstallLocation();
        if (aionInstallLocation == null)
        {
            aionInstallLocation = ConfigFile.DEFAULT_AION_LOCATION;
        }

        final File aionFolder = new File(aionInstallLocation);
        if (aionFolder.exists())
        {
            aionLocation.setText(aionInstallLocation);
            chooser.setInitialDirectory(aionFolder);
        }
        else
        {
            final File defaultDirectory = new File(ConfigFile.DEFAULT_AION_LOCATION);
            if (defaultDirectory.exists())
            {
                aionLocation.setText(ConfigFile.DEFAULT_AION_LOCATION);
                chooser.setInitialDirectory(defaultDirectory);
            }
            else
            {
                aionLocation.setText("Could not detect Aion install location");
            }
        }

        // Do a check after setting the directory
        checkFiles();
    }

}

package fx.screens;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

import org.apache.commons.io.FileUtils;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import main.MainFX;
import skins.Skins;

public class EnableChatLogScreen extends HBox
{

    private final double    CONTROL_SIZE       = 280;
    private final Label     ovrVer             = new Label("UNCHECKED");
    private final Label     chatVer            = new Label("UNCHECKED");
    private final TextField aionLocation       = new TextField();
    private final Button    chatBtn            = new Button("Fix This");
    private final Button    ovrBtn             = new Button("Fix This");
    final Button            saveAndFinalizeBtn = new Button("Save And Apply");
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

        aionLocation.setPrefWidth(260);
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

                    if (chatBtn.getText().contains("Clear"))
                    {
                        try
                        {
                            FileUtils.write(chatFileToMake, "", StandardCharsets.ISO_8859_1);
                        }
                        catch (final Exception e1)
                        {
                            final Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Did you run as Administrator?");
                            alert.setHeaderText(
                                    "Oops!\nIn order to clear the Chat.log file, ABM needs to be run in Administrator Mode.");
                            alert.setContentText(
                                    "To do this, close ABM, and restart it by right clicking, selecting Run As > Administrator.  "
                                            + "That will allow ABM to clear the Chat.log file");
                            alert.showAndWait();
                        }
                    }
                    else
                    {
                        if (!chatFileToMake.exists())
                        {
                            chatFileToMake.createNewFile();
                        }
                    }
                }
                catch (final IOException e1)
                {
                    final Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Did you run as Administrator?");
                    alert.setHeaderText(
                            "Oops!\nIn order to create the Chat.log file, ABM needs to be run in Administrator Mode.");
                    alert.setContentText(
                            "To do this, close ABM, and restart it by right clicking, selecting Run As > Administrator.  "
                                    + "That will allow ABM to create the Chat.log file");
                    alert.showAndWait();
                }

                checkFiles();
            }
        });
        final ImageView logHelp = new ImageView(IconLoader.loadFxImage("help-icon.png", 25));
        logHelp.setOnMouseClicked(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(final MouseEvent event)
            {
                final CustomAlert alrt = new CustomAlert("The Chat.log file",
                        "ABM reads the Chat.log file to work."
                                + "\nOver time the log file can get quite large and needs to be emptied.\n"
                                + "Emptying the log file has no adverse effects on ABM or your system.\n\n"
                                + "Note: ABM must be run in Administrator Mode to create or empty the Chat.log file");
                alrt.show();
            }
        });
        grid.add(new HBox(10, chatBtn, logHelp), 2, 2);

        ovrBtn.setPrefWidth(100);
        ovrBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                // Create the file if you can.

                if (ovrBtn.getText().equals("Turn Off"))
                {
                    try
                    {
                        final File cfgFileToCreate = new File(
                                aionLocation.getText() + "/" + ConfigFile.DEFAULT_CFG_FILE_NAME);
                        SystemConfigFileEditor.disableChatLogFile(cfgFileToCreate.getAbsolutePath());
                    }
                    catch (final Exception e1)
                    {
                        final Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Did you run as Administrator?");
                        alert.setHeaderText(
                                "Oops!\nIn order to disable the chat logging, ABM needs to be run in Administrator Mode.");
                        alert.setContentText(
                                "To do this, close ABM, and restart it by right clicking, selecting Run As > Administrator.  "
                                        + "That will allow ABM to edit the system.cfg file");
                        alert.showAndWait();
                    }
                }
                else
                {

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
                                "Oops!\nIn order to enable the chat logging, ABM needs to be run in Administrator Mode.");
                        alert.setContentText(
                                "To do this, close ABM, and restart it by right clicking, selecting Run As > Administrator.  "
                                        + "That will allow ABM to edit the system.cfg file");
                        alert.showAndWait();
                    }
                }

                checkFiles();
            }
        });
        final ImageView cfgHelp = new ImageView(IconLoader.loadFxImage("help-icon.png", 25));
        cfgHelp.setOnMouseClicked(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(final MouseEvent event)
            {
                final CustomAlert alrt = new CustomAlert("The system.cfg file",
                        "To enable / disable the chat log in Aion, ABM configures a value in the system.cfg file\n"
                                + "When the Aion client starts, it reads the system.cfg file and maintains the values in memory. "
                                + "When Aion closes, it writes the values back to the file.\n\n"
                                + "For this reason, Aion must be closed when changing this property.\n\n"
                                + "Note: ABM must be run in Administrator Mode to modify the system.cfg file and Aion must be closed during this process\n\n"
                                + "Note 2: If you are seeing performance issues during sieges or PVP events, disabling the logging may help.");
                alrt.show();
            }
        });
        grid.add(new HBox(10, ovrBtn, cfgHelp), 2, 3);

        // saveAndFinalizeBtn.setMinWidth(280);
        // saveAndFinalizeBtn.setMinHeight(40);
        // saveAndFinalizeBtn.setStyle("-fx-font-size: 25px;");
        saveAndFinalizeBtn.setAlignment(Pos.BOTTOM_RIGHT);
        saveAndFinalizeBtn.getStylesheets().add(Skins.get("GreenBtn.css"));

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
        grid.add(saveAndFinalizeBtn, 2, 4);

        this.getChildren().add(grid);
    }

    public void checkFiles()
    {
        final File chatFileCheck = new File(aionLocation.getText() + "/" + ConfigFile.DEFAULT_LOG_FILE_NAME);
        final File cfgFileCheck = new File(aionLocation.getText() + "/" + ConfigFile.DEFAULT_CFG_FILE_NAME);

        if (chatFileCheck.exists() && chatFileCheck.isFile() && chatFileCheck.canRead())
        {
            final double bytes = chatFileCheck.length();
            final double kilobytes = (bytes / 1024);
            final double megabytes = (kilobytes / 1024);
            final double gigabytes = (megabytes / 1024);

            final DecimalFormat df = new DecimalFormat("0.00");

            chatVer.setText("VERIFIED");
            chatVer.setTextFill(Color.DARKGREEN);
            chatBtn.setVisible(true);
            chatBtn.setText("Clear (" + df.format(gigabytes) + " GB)");
        }
        else
        {
            chatVer.setText("MISSING              -->");
            chatVer.setTextFill(Color.RED);
            chatBtn.setVisible(true);
            chatBtn.setText("Fix This");
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
                    ovrBtn.setVisible(true);
                    ovrBtn.setText("Turn Off");

                }
                else
                {
                    ovrVer.setText("NOT ENABLED                -->");
                    ovrVer.setTextFill(Color.LIGHTCORAL);
                    ovrBtn.setVisible(true);
                    ovrBtn.setText("Turn On");

                }
            }
            catch (final Exception e)
            {
                System.out.println(e);
                ovrVer.setText("ERROR IN READING               -->");
                ovrVer.setTextFill(Color.YELLOW);
                ovrBtn.setVisible(true);
                ovrBtn.setText("Fix This");
            }
        }
        else
        {
            ovrVer.setText("MISSING              -->");
            ovrVer.setTextFill(Color.RED);
            ovrBtn.setVisible(true);
            ovrBtn.setText("Fix this");
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

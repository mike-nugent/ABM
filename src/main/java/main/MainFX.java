package main;

import java.io.File;

import config.ConfigFile;
import database.AionDB;
import database.PlayerBaseUpdater;
import fx.buttons.ClockScreenButton;
import fx.buttons.ConfigWarningButton;
import fx.buttons.LogScreenButton;
import fx.buttons.MoveCursor;
import fx.buttons.OptionsButton;
import fx.buttons.PlayerScreenButton;
import fx.buttons.ScreenButton;
import fx.buttons.ScriptsScreenButton;
import fx.buttons.XformScreenButton;
import fx.screens.ScriptsUpdater;
import gameinfo.IconLoader;
import history.QuickHistoryLineScanner;
import history.RecentHistoryParser;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logreader.AionLogReader;
import sounds.SoundManager;
import triage.ExceptionHandler;

// Java 8 code
public class MainFX extends Application
{

    private double xOffset = 0;
    private double yOffset = 0;

    private final ProgressIndicator   _progressIcon        = new ProgressIndicator();
    private final ConfigWarningButton _configWarningButton = new ConfigWarningButton();
    private final OptionsButton       _optionsButton       = new OptionsButton();
    private final MoveCursor          _moveCursor          = new MoveCursor();

    // References back to this class instance for use by static methods.
    private static Stage  _primaryStage;
    private static MainFX _me;

    // Starts the loggers from the config page
    public static void jumpStartLoggers()
    {
        _me.startLoggers();
    }

    // Main entrance to the program
    public static void main(final String[] args)
    {
        launch(args);
    }

    public static void showStage()
    {
        if (_primaryStage != null)
        {
            _primaryStage.show();
        }
    }

    /**
     * Built in JavaFX mechanism for starting the program.
     *
     * @throws Exception
     */
    @Override
    public void start(final Stage primaryStage) throws Exception
    {
        try
        {
            _me = this;
            _primaryStage = primaryStage;
            setupDatabase();

            buildUI();
            startLoggers();
            loadConfigOptions();
        }
        catch (final Exception e)
        {
            ExceptionHandler.handleException(e);
        }
    }

    private void loadConfigOptions()
    {
        final String isSet = ConfigFile.getProperty(ConfigFile.LOCK_WINDOW_POSITION);
        if (isSet != null && isSet.equals("true"))
        {
            ASDMStage.setWindowLock(true);
        }
    }

    /**
     * Creates and displays the main UI
     */
    private void buildUI()
    {
        final HBox root = new HBox();
        root.setStyle("-fx-background-color: null;");

        setupStage(_primaryStage);

        // Create the buttons
        final HBox buttonsListBox = addScreenButtons();
        final HBox optionsListBox = addOptionsList();

        // Set the Progress Indicator for Async tasks
        _progressIcon.setMaxSize(25, 25);
        _progressIcon.setTooltip(new Tooltip("Performing a Quick Check on the Chat.log file"));

        // Add all children to the stage
        root.getChildren().addAll(buttonsListBox, optionsListBox);

        final Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        _primaryStage.setScene(scene);
        _primaryStage.show();

        final String stageLocation = ConfigFile.getProperty(ConfigFile.STAGE_LOCATION_PROPERTY);
        if (stageLocation != null)
        {
            final String[] XandY = stageLocation.split(",");
            final double winx = Double.parseDouble(XandY[0]);
            final double winy = Double.parseDouble(XandY[1]);

            _primaryStage.setX(winx);
            _primaryStage.setY(winy);
        }
    }

    private HBox addOptionsList()
    {

        // Set up wrappers for options buttons
        final HBox oWrapper = new HBox();
        final VBox leftBox = new VBox();
        final VBox rightBox = new VBox();

        leftBox.setAlignment(Pos.TOP_CENTER);
        rightBox.setAlignment(Pos.TOP_CENTER);

        leftBox.setSpacing(10);
        rightBox.setSpacing(10);

        leftBox.setPadding(new Insets(5, 5, 0, 0));
        rightBox.setPadding(new Insets(5, 0, 0, 5));

        leftBox.getChildren().addAll(_moveCursor, _configWarningButton);
        rightBox.getChildren().addAll(_optionsButton, _progressIcon);
        oWrapper.getChildren().addAll(leftBox, rightBox);
        oWrapper.setAlignment(Pos.TOP_RIGHT);
        return oWrapper;
    }

    private void setupDatabase()
    {
        AionDB.instantiate();
        PlayerBaseUpdater.initialize();
        ScriptsUpdater.initialize();
        SoundManager.initialize();
    }

    /**
     * Creates and starts the asynchronous scanners on the Chat.log file
     *
     * @param primaryStage
     */
    public void startLoggers()
    {
        // Quick verification the setup is correct:
        if (ConfigFile.isSetup())
        {
            _configWarningButton.setVisible(false);
            // Do some quick validation on the log file.
            final File logFile = ConfigFile.getLogFile();
            if (!logFile.exists())
            {
                DisplayManager.toggleConfigPopup();

                // If the log file doesn't exist, we need to create it.
                final Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Error in Setup");
                alert.setHeaderText("Check your logging settings");
                alert.setContentText("There may be an issue with your setup, check your settings.");
                return;
            }

            final QuickHistoryLineScanner scanner = new QuickHistoryLineScanner();
            final RecentHistoryParser parser = new RecentHistoryParser();

            final tasks.Task task = scanner.scanFile(parser);
            _progressIcon.setVisible(true);

            new AnimationTimer()
            {
                @Override
                public void handle(final long now)
                {
                    if (task.isTerminal())
                    {
                        _progressIcon.setVisible(false);
                        System.out.println("Stopping thread");
                        this.stop();
                    }
                }
            }.start();

            if (!AionLogReader.isRunning())
            {
                AionLogReader.readLog();
            }
        }
        else
        {
            // Is no set up. Spawn window and yell at user
            _configWarningButton.setVisible(true);
            DisplayManager.toggleConfigPopup();
            _primaryStage.hide();

            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Welcome Daeva!");
            alert.setHeaderText("Welcome Daeva!\nLet's set up your Aion Sovereign Daeva Meter");
            alert.setContentText("We will need some iformation about your character and setup.\n\n"
                    + "Please enter information on the following screens to get started.");
            alert.setGraphic(new ImageView(IconLoader.loadFxImage("faction2.png", 60)));
            alert.showAndWait();
        }
    }

    /**
     * Sets common functionality of the main stage
     */
    private void setupStage(final Stage primaryStage)
    {
        ASDMStage.setStage(primaryStage);

        primaryStage.setAlwaysOnTop(true);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // _moveCursor.setEffect(new DropShadow(10, Color.DARKGRAY));
        _moveCursor.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                if (ASDMStage.getWindowLock())
                {
                    return;
                }

                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        _moveCursor.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                if (ASDMStage.getWindowLock())
                {
                    return;
                }
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
        _moveCursor.setOnMouseReleased(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                if (ASDMStage.getWindowLock())
                {
                    return;
                }

                // When we drag the screen around and then release the mouse,
                // save the screen location to the file.
                // So when we open the program again, it remembers where we
                // positioned it.
                ConfigFile.setProperty(ConfigFile.STAGE_LOCATION_PROPERTY,
                        primaryStage.getX() + "," + primaryStage.getY());
            }
        });
    }

    /**
     * Create the main buttons on the overlay display
     *
     */
    public HBox addScreenButtons()
    {
        final HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(15);
        hbox.setAlignment(Pos.CENTER);
        final XformScreenButton xform = new XformScreenButton();
        DisplayManager.setXformButton(xform);
        final ScreenButton players = new PlayerScreenButton();
        final ScreenButton scripts = new ScriptsScreenButton();
        final ScreenButton logs = new LogScreenButton();
        final ClockScreenButton clock = new ClockScreenButton();

        hbox.getChildren().addAll(xform, players, scripts, logs, clock);

        return hbox;
    }

}
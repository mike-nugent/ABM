package main;

import java.io.File;

import config.ConfigFile;
import database.AionDB;
import database.PlayerBaseUpdater;
import fx.buttons.ClockScreenButton;
import fx.buttons.ConfigWarningButton;
import fx.buttons.LogScreenButton;
import fx.buttons.OptionsButton;
import fx.buttons.PlayerScreenButton;
import fx.buttons.ScreenButton;
import fx.buttons.ScriptsScreenButton;
import fx.buttons.XformScreenButton;
import gameinfo.IconLoader;
import history.QuickHistoryLineScanner;
import history.RecentHistoryParser;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logreader.AionLogReader;

// Java 8 code
public class MainFX extends Application
{

    private double xOffset = 0;
    private double yOffset = 0;

    private final ProgressIndicator   _progressIcon        = new ProgressIndicator();
    private final ConfigWarningButton _configWarningButton = new ConfigWarningButton();
    private final OptionsButton       _optionsButton       = new OptionsButton();

    // References back to this class instance for use by static methods.
    private static Stage  _primaryStage;
    private static MainFX _me;

    // The current width and height of the stage.
    private static int STAGE_WIDTH  = 480;
    private static int STAGE_HEIGHT = 160;

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
     */
    @Override
    public void start(final Stage primaryStage)
    {
        _me = this;
        _primaryStage = primaryStage;
        setupDatabase();

        buildUI();
        startLoggers();
        loadConfigOptions();
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
        //
        final Pane root = new Pane();
        final DoubleProperty doubleProperty = new SimpleDoubleProperty(0);
        final Region background = setBackground(root, doubleProperty);

        setupStage(_primaryStage, background);

        // Create the buttons
        final HBox buttonsListBox = addScreenButtons();
        final HBox optionsListBox = addOptionsList();

        // Create the Slider
        final String lastSliderPosition = ConfigFile.getProperty(ConfigFile.SLIDER_POSITION_PROPERTY);
        final double initialValue = (lastSliderPosition != null) ? Double.parseDouble(lastSliderPosition) : 0.3;
        final StackPane slider = createSlider(doubleProperty, initialValue);

        // Set the Progress Indicator for Async tasks
        _progressIcon.setMaxSize(25, 25);
        _progressIcon.setTooltip(new Tooltip("Performing a Quick Check on the Chat.log file"));

        // Add all children to the stage
        root.getChildren().addAll(buttonsListBox, optionsListBox, slider);

        optionsListBox.setLayoutX(STAGE_WIDTH - 90);
        optionsListBox.setLayoutY(0);

        // Reposition them all
        buttonsListBox.setLayoutX(10);
        buttonsListBox.setLayoutY(-10);

        slider.setMinWidth(STAGE_WIDTH);
        slider.setLayoutY(STAGE_HEIGHT - 40);
        slider.setLayoutX(45);

        root.setMinWidth(STAGE_WIDTH);
        root.setMinHeight(STAGE_HEIGHT);

        final Scene scene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT);
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
        /*
         * <pre> ----------- | | | | A | B | | | | ----------- | | | | | C | | |
         * | ----------- </pre>
         */

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

        leftBox.getChildren().add(_configWarningButton);
        rightBox.getChildren().addAll(_optionsButton, _progressIcon);
        oWrapper.getChildren().addAll(leftBox, rightBox);
        oWrapper.setAlignment(Pos.TOP_RIGHT);
        return oWrapper;
    }

    private void setupDatabase()
    {
        AionDB.instantiate();
        PlayerBaseUpdater.initialize();
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
                TransformManager.toggleConfigPopup();

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
            TransformManager.toggleConfigPopup();
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
     * Sets the background of the main app display
     */
    private Region setBackground(final Pane root, final DoubleProperty doubleProperty)
    {
        final Region region = new Region();
        region.setMinWidth(STAGE_WIDTH - 20);
        region.setMinHeight(STAGE_HEIGHT - 20);
        region.styleProperty()
                .bind(Bindings.concat("-fx-background-radius:20; -fx-background-color: rgba(109, 155, 155, ")
                        .concat(doubleProperty).concat(");"));
        region.setEffect(new DropShadow(10, Color.BLACK));
        root.getChildren().addAll(region);
        root.setStyle("-fx-background-color: null;");
        root.setPadding(new Insets(10));
        return region;
    }

    /**
     * Sets common functionality of the main stage
     */
    private void setupStage(final Stage primaryStage, final Region root)
    {
        ASDMStage.setStage(primaryStage);

        primaryStage.setAlwaysOnTop(true);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        root.setOnMousePressed(new EventHandler<MouseEvent>()
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
        root.setOnMouseDragged(new EventHandler<MouseEvent>()
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
        root.setOnMouseReleased(new EventHandler<MouseEvent>()
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
     * Create the slider used to scale background alpha
     */
    private StackPane createSlider(final DoubleProperty doubleProperty, final double initialValue)
    {
        final Slider slider = new Slider(0.01, 0.9, initialValue);

        slider.setOnMouseReleased(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                ConfigFile.setProperty(ConfigFile.SLIDER_POSITION_PROPERTY, slider.getValue() + "");
            }
        });

        final StackPane sliderPane = new StackPane();
        sliderPane.setAlignment(Pos.BOTTOM_LEFT);
        sliderPane.getChildren().add(slider);
        slider.setMaxWidth(STAGE_WIDTH - 90);
        doubleProperty.bind(slider.valueProperty());
        return sliderPane;
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
        final XformScreenButton xform = new XformScreenButton();
        TransformManager.setXformButton(xform);
        final ScreenButton players = new PlayerScreenButton();
        final ScreenButton scripts = new ScriptsScreenButton();
        final ScreenButton logs = new LogScreenButton();
        final ClockScreenButton clock = new ClockScreenButton();

        hbox.getChildren().addAll(xform, players, scripts, logs, clock);

        return hbox;
    }

}
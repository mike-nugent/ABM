package main;

import java.io.File;
import java.util.Optional;

import config.ConfigFile;
import database.AionDB;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.timer.FxClock;
import javafx.timer.FxClockController;
import logreader.AionLogReader;

// Java 8 code
public class MainFX extends Application
{

    private double        xOffset = 0;
    private double        yOffset = 0;
    protected ContextMenu _optionsMenu;

    private ProgressIndicator   _progressIcon;
    private ConfigWarningButton _configWarningButton;
    private static Stage        _primaryStage;

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
        _primaryStage = primaryStage;
        buildUI();
        startLoggers();
        setupDatabase();
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
        final HBox hbox = addScreenButtons();
        // hbox.setAlignment(Pos.TOP_CENTER);

        // Create the timer
        final FxClock clock = new FxClock();
        final FxClockController clockController = new FxClockController(clock);
        clockController.setLayoutX(300);
        clockController.setLayoutY(100);

        // Create the Slider
        final String lastSliderPosition = ConfigFile.getProperty(ConfigFile.SLIDER_POSITION_PROPERTY);
        final double initialValue = (lastSliderPosition != null) ? Double.parseDouble(lastSliderPosition) : 0.3;
        final StackPane slider = createSlider(doubleProperty, initialValue);

        // Set the Progress Indicator for Async tasks
        _progressIcon = new ProgressIndicator();
        _progressIcon.setMaxSize(25, 25);
        _progressIcon.setTooltip(new Tooltip("Performing a Quick Check on the Chat.log file"));

        // Create the options icon button
        final OptionsButton optionsButton = new OptionsButton();
        _configWarningButton = new ConfigWarningButton();
        _configWarningButton.setVisible(false);

        // Add all children to the stage
        root.getChildren().addAll(hbox, clock, clockController, _progressIcon, optionsButton, _configWarningButton,
                slider);

        // Reposition them all
        hbox.setLayoutX(10);
        hbox.setLayoutY(-10);

        clock.setLayoutX(20);
        clock.setLayoutY(55);

        slider.setMinWidth(300);
        slider.setLayoutY(160);
        slider.setLayoutX(45);

        optionsButton.setLayoutX(350);
        optionsButton.setLayoutY(15);

        _configWarningButton.setLayoutX(315);
        _configWarningButton.setLayoutY(15);

        _progressIcon.setLayoutX(350);
        _progressIcon.setLayoutY(50);

        root.setMinWidth(400);
        root.setMinHeight(200);

        final Scene scene = new Scene(root, 400, 200);
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

    private void setupDatabase()
    {
        AionDB.instantiate();
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
                // If the log file doesn't exist, we need to create it.
                final Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Log File Missing");
                alert.setHeaderText("Logging is not enabled in your aion setup");
                alert.setContentText(
                        "To use ASDM, logging must be enabled.\n" + "We recommend enabling it now.  Proceed?");

                final Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                {
                    // ... user chose OK
                }
                else
                {
                    // ... user chose CANCEL or closed the dialog
                }
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
        region.setMinWidth(380);
        region.setMinHeight(180);
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
        sliderPane.setAlignment(Pos.BOTTOM_CENTER);
        sliderPane.getChildren().add(slider);
        slider.setMaxWidth(300);
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

        hbox.getChildren().addAll(xform, players, scripts, logs);

        return hbox;
    }

}
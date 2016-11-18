package main;

import config.ConfigFile;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logreader.AionLogReader;

// Java 8 code
public class HelloWorld extends Application
{

    private double        xOffset = 0;
    private double        yOffset = 0;
    protected ContextMenu _optionsMenu;

    private ProgressIndicator _progressIcon;

    public static void main(final String[] args)
    {
        launch(args);
    }

    /**
     * Built in JavaFX mechanism for starting the program.
     */
    @Override
    public void start(final Stage primaryStage)
    {
        buildUI(primaryStage);
        ;
        startLoggers();
    }

    /**
     * Creates and displays the main UI
     */
    private void buildUI(final Stage primaryStage)
    {
        final Pane root = new Pane();
        final DoubleProperty doubleProperty = new SimpleDoubleProperty(0);
        final Region background = setBackground(root, doubleProperty);

        setupStage(primaryStage, background);

        // Create the buttons
        final HBox hbox = addScreenButtons();
        // hbox.setAlignment(Pos.TOP_CENTER);

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
        final ConfigWarningButton configWarningButton = new ConfigWarningButton();

        // Add all children to the stage
        root.getChildren().addAll(hbox, _progressIcon, optionsButton, configWarningButton, slider);

        // Reposition them all
        hbox.setLayoutX(10);
        hbox.setLayoutY(-10);

        slider.setMinWidth(300);
        slider.setLayoutY(160);
        slider.setLayoutX(45);

        optionsButton.setLayoutX(350);
        optionsButton.setLayoutY(15);

        configWarningButton.setLayoutX(315);
        configWarningButton.setLayoutY(15);

        _progressIcon.setLayoutX(350);
        _progressIcon.setLayoutY(50);

        root.setMinWidth(400);
        root.setMinHeight(200);

        final Scene scene = new Scene(root, 400, 200);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();

        final String stageLocation = ConfigFile.getProperty(ConfigFile.STAGE_LOCATION_PROPERTY);
        if (stageLocation != null)
        {
            final String[] XandY = stageLocation.split(",");
            final double winx = Double.parseDouble(XandY[0]);
            final double winy = Double.parseDouble(XandY[1]);

            primaryStage.setX(winx);
            primaryStage.setY(winy);
        }
    }

    /**
     * Creates and starts the asynchronous scanners on the Chat.log file
     */
    public void startLoggers()
    {
        // Quick verification the setup is correct:
        if (ConfigFile.isSetup())
        {
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
            ;

            if (!AionLogReader.isRunning())
            {
                AionLogReader.readLog();
            }
        }
        else
        {
            // Is no set up. Spawn window and yell at user
            System.out.println("Yell at user");
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
        hbox.setSpacing(10);
        final XformScreenButton xform = new XformScreenButton();
        TransformManager.setXformButton(xform);
        final ScreenButton world = new ScreenButton("aion_area.png", 60);
        final ScreenButton scripts = new LogScreenButton();
        final ScreenButton faction = new ScreenButton("faction.png", 60);

        hbox.getChildren().addAll(xform, world, scripts, faction);

        return hbox;
    }

}
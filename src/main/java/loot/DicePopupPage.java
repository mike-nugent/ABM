package loot;

import config.ConfigFile;
import fx.buttons.MoveCursor;
import gameinfo.IconLoader;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import main.ABMStage;
import main.DisplayManager;

public class DicePopupPage extends Stage
{
    private double           xOffset          = 0;
    private double           yOffset          = 0;
    static DicePopupPage     me;
    private final MoveCursor _moveCursor      = new MoveCursor();
    private final Pane       _diceIcon        = new Pane(new ImageView(IconLoader.loadFxImage("dice-icon.png", 40)));
    Label                    lastToRoll       = makeLabel(new ImageView(IconLoader.loadFxImage("late-icon.png", 16)), "", Color.CORAL, 16);
    Label                    activeRollField  = makeLabel(new ImageView(IconLoader.loadFxImage("white-dice.png", 16)), "", Color.LAWNGREEN, 16);
    Label                    winsRoll         = makeLabel(new ImageView(IconLoader.loadFxImage("win-icon.png", 16)), "", Color.GOLD, 16);
    static String            lastPersonToRoll = "";

    public DicePopupPage()
    {
        me = this;
        this.setAlwaysOnTop(true);
        this.initStyle(StageStyle.TRANSPARENT);
        positionListener(_moveCursor);

        addInteractionListeners(_diceIcon);

        final VBox displayWrapper = new VBox();
        displayWrapper.setSpacing(10);
        displayWrapper.setStyle("-fx-background-color: null;");

        final VBox rightSide = new VBox();

        rightSide.getChildren().addAll(lastToRoll, activeRollField, winsRoll);
        rightSide.setEffect(new DropShadow(2, Color.BLACK));

        _diceIcon.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                DisplayManager.toggleLootPopup();
            }
        });

        final VBox leftSide = new VBox(_diceIcon);
        leftSide.setAlignment(Pos.CENTER);
        final HBox hbox = new HBox(leftSide, rightSide);
        hbox.setSpacing(10);

        displayWrapper.getChildren().addAll(_moveCursor, hbox);

        final Scene scene = new Scene(displayWrapper);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
        _moveCursor.fadeOutWhenExit(displayWrapper, _moveCursor);

        final String stageLocation = ConfigFile.getProperty(ConfigFile.DICE_LOCATION_PROPERTY);
        if (stageLocation != null)
        {
            final String[] XandY = stageLocation.split(",");
            final double winx = Double.parseDouble(XandY[0]);
            final double winy = Double.parseDouble(XandY[1]);

            me.setX(winx);
            me.setY(winy);
        }
        this.sizeToScene();
    }

    private void addInteractionListeners(final Pane dice)
    {
        dice.setEffect(new DropShadow(5, Color.DARKGRAY));

        dice.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent evt)
            {
                dice.setEffect(new DropShadow(5, Color.web("#ffffb3")));
            }
        });

        dice.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent evt)
            {
                dice.setEffect(new DropShadow(5, Color.DARKGRAY));
            }
        });
    }

    public static void showPlayerRoll(final String person, final int roll)
    {
        lastPersonToRoll = person;
        String rollTxt = roll + "";
        if (roll == 0)
        {
            rollTxt = "pass";
        }
        me.activeRollField.setText(person + " " + rollTxt);
        me.sizeToScene();
        me.activeRollField.setOpacity(1.0);
        final FadeTransition fade = me.getFade(me.activeRollField);
        fade.play();
    }

    public static void updateWinsRoll(final String person, final int roll)
    {
        me.winsRoll.setText(person + " " + roll);
        me.sizeToScene();
        me.winsRoll.setOpacity(1.0);
        final FadeTransition fade = me.getFade(me.winsRoll);
        fade.play();

        updateLastToRoll(lastPersonToRoll);
    }

    public static void updateLastToRoll(final String person)
    {
        me.lastToRoll.setText(person);
        me.sizeToScene();
        me.lastToRoll.setOpacity(1.0);
        final FadeTransition fade = me.getFade(me.lastToRoll);
        fade.play();
    }

    public FadeTransition getFade(final Label label)
    {
        final FadeTransition fade = new FadeTransition(Duration.seconds(30), label);
        fade.setFromValue(1.0);
        fade.setToValue(0);
        return fade;
    }

    private Label makeLabel(final ImageView image, final String txt, final Color color, final int size)
    {
        final Label label = new Label(txt, image);
        label.setTextFill(color);
        label.setFont(Font.font(null, FontWeight.BOLD, size));
        return label;
    }

    public void positionListener(final Pane pane)
    {
        pane.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                if (ABMStage.getWindowLock())
                {
                    return;
                }

                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        pane.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                if (ABMStage.getWindowLock())
                {
                    return;
                }
                me.setX(event.getScreenX() - xOffset);
                me.setY(event.getScreenY() - yOffset);
            }
        });
        pane.setOnMouseReleased(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                if (ABMStage.getWindowLock())
                {
                    return;
                }

                // When we drag the screen around and then release the mouse,
                // save the screen location to the file.
                // So when we open the program again, it remembers where we
                // positioned it.
                ConfigFile.setProperty(ConfigFile.DICE_LOCATION_PROPERTY, me.getX() + "," + me.getY());
            }
        });
    }

    public void showCursorForASecond()
    {
        _moveCursor.setOpacity(1);
        final FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), _moveCursor);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.play();
    }
}

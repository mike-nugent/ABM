package loot;

import java.util.HashMap;
import java.util.Map;

import config.ConfigFile;
import fx.buttons.MoveCursor;
import gameinfo.IconLoader;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import main.ABMStage;

public class LootPopupPage extends Stage
{
    private double xOffset = 0;
    private double yOffset = 0;

    public static LootPopupPage me;

    HBox            playerHBox = new HBox(10);
    final ImageView btn        = new ImageView(IconLoader.loadFxImage("reset-icon.png", 30));

    Map<String, PlayerLootTable> looters     = new HashMap<String, PlayerLootTable>();
    private final MoveCursor     _moveCursor = new MoveCursor();

    public LootPopupPage()
    {
        me = this;
        this.setAlwaysOnTop(true);
        this.initStyle(StageStyle.TRANSPARENT);
        final VBox displayWrapper = new VBox();
        displayWrapper.setSpacing(10);
        displayWrapper.setStyle("-fx-background-color: null;");
        final Scene scene = new Scene(displayWrapper);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);

        btn.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                playerHBox.getChildren().removeAll(playerHBox.getChildren());
                looters.clear();
            }
        });

        playerHBox.setPadding(new Insets(10));
        _moveCursor.fadeOutWhenExit(displayWrapper, _moveCursor);
        positionListener(_moveCursor);
        displayWrapper.getChildren().addAll(_moveCursor, btn, playerHBox);

        final String stageLocation = ConfigFile.getProperty(ConfigFile.LOOT_LOCATION_PROPERTY);
        if (stageLocation != null)
        {
            final String[] XandY = stageLocation.split(",");
            final double winx = Double.parseDouble(XandY[0]);
            final double winy = Double.parseDouble(XandY[1]);

            me.setX(winx);
            me.setY(winy);
        }
    }

    public void showCursorForASecond()
    {
        _moveCursor.setOpacity(1);
        final FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), _moveCursor);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.play();
    }

    public static void showNewLootRecieved(final String person, final int totalNumItems, final String itemID)
    {
        me.showNewLoot(person, totalNumItems, itemID);
    }

    protected void showNewLoot(final String person, final int totalNumItems, final String itemID)
    {
        // Get or create box for the person
        final PlayerLootTable playerBox = getOrCreatePlayer(person);
        playerBox.addItem(itemID, totalNumItems);
        me.sizeToScene();
    }

    private PlayerLootTable getOrCreatePlayer(final String person)
    {
        PlayerLootTable plt = looters.get(person);
        if (plt == null)
        {
            plt = new PlayerLootTable(person);
            looters.put(person, plt);
            playerHBox.getChildren().add(plt);
        }

        return plt;
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
                ConfigFile.setProperty(ConfigFile.LOOT_LOCATION_PROPERTY, me.getX() + "," + me.getY());
            }
        });
    }

}

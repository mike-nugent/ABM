package fx.buttons;

import gameinfo.IconLoader;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.ABMStage;

public class MoveCursor extends Pane
{
    private final Pane _moveCursor = new Pane(new ImageView(IconLoader.loadFxImage("move-cursor.png", 25)));

    public MoveCursor()
    {
        this.getChildren().add(_moveCursor);
        _moveCursor.setEffect(new DropShadow(10, Color.DARKGRAY));

        _moveCursor.setOnMouseEntered(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent me)
            {
                if (ABMStage.getWindowLock())
                {
                    return;
                }
                _moveCursor.getScene().setCursor(Cursor.MOVE); // Change cursor
                                                               // to hand
            }
        });
        _moveCursor.setOnMouseExited(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent me)
            {
                _moveCursor.getScene().setCursor(Cursor.DEFAULT); // Change
                                                                  // cursor to
                                                                  // hand
            }
        });
    }
}

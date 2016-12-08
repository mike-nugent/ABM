package main;

import gameinfo.IconLoader;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class LockFx extends StackPane
{
    final ImageView openLock  = new ImageView(IconLoader.loadFxImage("open-lock.png", 25));
    final ImageView closeLock = new ImageView(IconLoader.loadFxImage("close-lock.png", 25));
    boolean         isLocked  = false;

    public LockFx()
    {
        this.getChildren().addAll(openLock, closeLock);

        this.setLocked(isLocked);
        this.setEffect(new DropShadow(10, Color.DARKGRAY));

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                toggleLock();
            }
        });
    }

    public void setLocked(final boolean state)
    {
        this.isLocked = state;
        closeLock.setVisible(state);
        openLock.setVisible(!state);
    }

    private void toggleLock()
    {
        if (isLocked)
        {
            setLocked(false);
        }
        else
        {
            setLocked(true);
        }
    }
}

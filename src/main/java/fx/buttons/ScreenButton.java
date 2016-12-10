package fx.buttons;

import gameinfo.IconLoader;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ScreenButton extends StackPane
{
    ScreenButton           me;
    protected final Region region = new Region();

    public ScreenButton(final String iconName, final int height)
    {
        me = this;
        final Image image = IconLoader.loadFxImage(iconName, height);
        final ImageView imageView = new ImageView(image);

        this.getChildren().add(imageView);
        me.setEffect(new DropShadow(10, Color.DARKGRAY));

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                mousePressed(event);
                // me.setEffect(new DropShadow(10, Color.RED));

            }

        });

        this.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent evt)
            {
                me.setEffect(new DropShadow(15, Color.web("#ffffb3")));
                mouseEntered(evt);
            }

        });

        this.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent evt)
            {
                me.setEffect(new DropShadow(10, Color.DARKGRAY));
                mouseExited(evt);
            }

        });
    }

    protected void setupRegion(final Region region, final int width, final int height)
    {
        // region.setMinWidth(60);
        // region.setMinHeight(60);
        region.setPrefWidth(width);
        region.setPrefHeight(height);
        region.setStyle("-fx-background-radius:15; -fx-background-color: rgba(255, 255, 153, 0.5);");
        // region.setEffect(new DropShadow(10, Color.BLACK));
        this.getChildren().add(0, region);
    }

    protected void mousePressed(final MouseEvent event)
    {

    }

    protected void mouseEntered(final MouseEvent event)
    {

    }

    protected void mouseExited(final MouseEvent event)
    {

    }

}

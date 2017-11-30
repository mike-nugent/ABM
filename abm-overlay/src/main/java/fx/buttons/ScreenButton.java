package fx.buttons;

import gameinfo.IconLoader;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ScreenButton extends StackPane
{
    ScreenButton              me;
    final ImageView           imageView = new ImageView();
    protected final StackPane background;
    private final boolean     isToggle;
    private boolean           isOpen;

    public void updateImageDimensions(final int size)
    {
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        background.setPrefHeight(size);
        background.setPrefWidth(size);
    }

    public ScreenButton(final String iconName, final int size)
    {
        this(iconName, size, false);
    }

    public ScreenButton(final String iconName, final int size, final boolean isToggle)
    {
        me = this;
        this.isToggle = isToggle;

        final Image image = IconLoader.loadFxImage(iconName, size);
        imageView.setImage(image);

        background = new StackPane(imageView);
        background.setStyle("-fx-background-color: rgba(0,0,0,0.01)");
        background.setPrefHeight(size);
        background.setPrefWidth(size);

        this.getChildren().add(background);
        background.setEffect(new DropShadow(5, Color.DARKGRAY));

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
                mousePressed(event);

                if (isToggle)
                {
                    if (isOpen())
                    {
                        setIsOpen(false);
                        closeTriggered();
                    }
                    else
                    {
                        setIsOpen(true);
                        openTriggered();
                    }
                }

            }

        });

        this.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent evt)
            {
                background.setEffect(new DropShadow(5, Color.web("#ffffb3")));
                mouseEntered(evt);
            }

        });

        this.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent evt)
            {
                background.setEffect(new DropShadow(5, Color.DARKGRAY));
                mouseExited(evt);
            }

        });
    }

    protected void closeTriggered()
    {
        // TODO Auto-generated method stub

    }

    protected void openTriggered()
    {
        // TODO Auto-generated method stub

    }

    protected void setIsOpen(final boolean value)
    {
        this.isOpen = value;
        if (!isOpen)
        {
            background.setStyle("-fx-background-color: rgba(0,0,0,0.01)");
        }
        else
        {
            background.setStyle("-fx-background-color: rgba(255,0,0,0.4)");
        }
    }

    protected boolean isOpen()
    {
        return isOpen;
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

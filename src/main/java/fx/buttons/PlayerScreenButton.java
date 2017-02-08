package fx.buttons;

import javafx.scene.input.MouseEvent;
import main.DisplayManager;

public class PlayerScreenButton extends ScreenButton
{

    public PlayerScreenButton(final int size)
    {
        super("search_icon.png", size);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void mousePressed(final MouseEvent event)
    {
        DisplayManager.togglePlayersPopup();

        // uncomment to test adding a transform
        // PlayerData random = PlayerData.generateRandom();
        // TransformManager.transformDetected(random);
    }

    @Override
    protected void mouseEntered(final MouseEvent event)
    {
        /// TransformManager.toggleTransformPopup();

    }

    @Override
    protected void mouseExited(final MouseEvent event)
    {
        /// TransformManager.toggleTransformPopup();

    }
}

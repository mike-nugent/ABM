package fx.screens;

import javafx.scene.input.MouseEvent;
import main.ScreenButton;
import main.TransformManager;

public class PlayerScreenButton extends ScreenButton
{

    public PlayerScreenButton()
    {
        super("aion_lore.png", 60);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void mousePressed(final MouseEvent event)
    {
        TransformManager.togglePlayersPopup();

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

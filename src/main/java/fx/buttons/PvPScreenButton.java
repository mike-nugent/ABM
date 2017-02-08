package fx.buttons;

import fx.screens.AdditionalToolMenu;
import javafx.scene.input.MouseEvent;

public class PvPScreenButton extends ScreenButton
{
    public PvPScreenButton(final int size)
    {
        super("swords-icon.gif", size);
    }

    @Override
    protected void mousePressed(final MouseEvent event)
    {
        AdditionalToolMenu.openToolMenu();
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

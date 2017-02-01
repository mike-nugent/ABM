package fx.buttons;

import fx.screens.AdditionalToolMenu;
import fx.screens.OptionsMenu;
import javafx.scene.input.MouseEvent;

public class PvPScreenButton extends ScreenButton
{
	public PvPScreenButton() 
	{
		super("swords-icon.gif", 60);
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

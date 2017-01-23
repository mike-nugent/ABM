package main;

import config.ConfigFile;
import fx.buttons.ScreenButton;
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
        if (!DisplayManager.isPvPShowing())
        {
            DisplayManager.showPvPPopup();
            region.setVisible(false);
        }
        else
        {
            DisplayManager.hidePvPPopup();
            region.setVisible(true);
        }
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

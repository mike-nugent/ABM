package fx.buttons;

import config.ConfigFile;
import javafx.scene.input.MouseEvent;
import main.DisplayManager;

public class ClockScreenButton extends ScreenButton
{
    public ClockScreenButton()
    {
        super("clock_icon.png", 60);

        setupRegion(region, 50, 70);

        final String isShowing = ConfigFile.getProperty(ConfigFile.IS_CLOCK_SHOWING);

        if (isShowing != null && isShowing.equals("true"))
        {
            DisplayManager.showClockPopup();
            region.setVisible(true);
        }
        else
        {
            DisplayManager.hideClockPopup();
            region.setVisible(false);
        }
    }

    @Override
    protected void mousePressed(final MouseEvent event)
    {
        if (DisplayManager.isClockShowing())
        {
            DisplayManager.hideClockPopup();
            region.setVisible(false);
            ConfigFile.setProperty(ConfigFile.IS_CLOCK_SHOWING, "false");
        }
        else
        {
            DisplayManager.showClockPopup();
            region.setVisible(true);
            ConfigFile.setProperty(ConfigFile.IS_CLOCK_SHOWING, "true");
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

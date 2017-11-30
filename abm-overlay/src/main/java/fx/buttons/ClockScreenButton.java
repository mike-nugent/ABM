package fx.buttons;

import config.ConfigFile;
import javafx.scene.input.MouseEvent;
import main.DisplayManager;

public class ClockScreenButton extends ScreenButton
{
    public ClockScreenButton(final int size)
    {
        super("clock_icon.png", size, true);

        final String isShowing = ConfigFile.getProperty(ConfigFile.IS_CLOCK_SHOWING);

        if (isShowing != null && isShowing.equals("true"))
        {
            DisplayManager.showClockPopup();
            this.setIsOpen(true);
        }
        else
        {
            DisplayManager.hideClockPopup();
            this.setIsOpen(false);
        }
    }

    @Override
    protected void closeTriggered()
    {
        DisplayManager.hideClockPopup();
        ConfigFile.setProperty(ConfigFile.IS_CLOCK_SHOWING, "false");
    }

    @Override
    protected void openTriggered()
    {
        DisplayManager.showClockPopup();
        ConfigFile.setProperty(ConfigFile.IS_CLOCK_SHOWING, "true");
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

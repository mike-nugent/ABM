package fx.buttons;

import javafx.scene.input.MouseEvent;
import main.DisplayManager;

public class ScriptsScreenButton extends ScreenButton
{
    public ScriptsScreenButton(final int size)
    {
        super("aion_craft.png", size);
    }

    @Override
    protected void mousePressed(final MouseEvent event)
    {
        DisplayManager.toggleScriptsPopup();

    }

    @Override
    protected void mouseEntered(final MouseEvent event)
    {

    }

    @Override
    protected void mouseExited(final MouseEvent event)
    {

    }
}

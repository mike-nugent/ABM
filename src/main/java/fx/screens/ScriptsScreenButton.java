package fx.screens;

import javafx.scene.input.MouseEvent;
import main.ScreenButton;
import main.TransformManager;

public class ScriptsScreenButton extends ScreenButton
{
    public ScriptsScreenButton()
    {
        super("aion_craft.png", 60);
    }

    @Override
    protected void mousePressed(final MouseEvent event)
    {
        TransformManager.toggleScriptsPopup();

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

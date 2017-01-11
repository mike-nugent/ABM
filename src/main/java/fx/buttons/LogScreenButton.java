package fx.buttons;

import fx.screens.AlertScreen;
import javafx.scene.input.MouseEvent;
import main.DisplayManager;

public class LogScreenButton extends ScreenButton
{

    public LogScreenButton()
    {
        super("log.png", 60);
    }

    @Override
    protected void mousePressed(final MouseEvent event)
    {
        DisplayManager.toggleLogsPopup();
        // AlertScreen.showAlert("Hi mom");
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

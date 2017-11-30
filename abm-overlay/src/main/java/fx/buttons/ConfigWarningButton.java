package fx.buttons;

import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import main.DisplayManager;

public class ConfigWarningButton extends ScreenButton
{
    public ConfigWarningButton()
    {
        super("warning.png", 25);
        this.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(this, Priority.ALWAYS); // Give stack any extra space
    }

    @Override
    protected void mousePressed(final MouseEvent event)
    {
        DisplayManager.openLoggingSettings();
    }
}

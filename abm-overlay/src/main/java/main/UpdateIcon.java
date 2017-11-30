package main;

import java.awt.Desktop;
import java.net.URI;

import fx.buttons.ScreenButton;
import fx.screens.CustomAlert;
import fx.screens.UpdateAvailableAlert;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import versioning.VersionManager;

public class UpdateIcon extends ScreenButton
{
    public UpdateIcon()
    {
        super("update-icon.png", 25);
        this.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(this, Priority.ALWAYS); // Give stack any extra space
        
    }

    @Override
    protected void mousePressed(final MouseEvent event)
    {
    	VersionManager.checkForNewerVersions();
    }
}

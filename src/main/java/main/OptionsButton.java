package main;

import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class OptionsButton extends ScreenButton 
{
	public OptionsButton() 
	{
		super("gears.png", 25);
		this.setAlignment(Pos.TOP_RIGHT);
		HBox.setHgrow(this, Priority.ALWAYS); // Give stack any extra space
	}

	@Override
	protected void mousePressed(MouseEvent event) 
	{
		OptionsMenu.openOptionsMenu();
	}
}

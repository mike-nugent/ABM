package fx.buttons;

import config.ConfigFile;
import javafx.beans.binding.Bindings;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import main.TransformManager;

public class ClockScreenButton extends ScreenButton 
{

	private Region region = new Region();

	public ClockScreenButton() 
	{
		super("clock_icon.png", 60);
		
		setupRegion(region);

		String isShowing =  ConfigFile.getProperty(ConfigFile.IS_CLOCK_SHOWING);
		
		if (isShowing != null && isShowing.equals("true"))
		{
			TransformManager.showClockPopup();
			region.setVisible(true);
		} 
		else 
		{
			TransformManager.hideClockPopup();
			region.setVisible(false);

		}
	}

	private void setupRegion(Region region2) 
	{
		//region.setMinWidth(60);
		//region.setMinHeight(60);
		region.setMaxWidth(50);
		region.setMaxHeight(70);
		region.setStyle("-fx-background-radius:15; -fx-background-color: rgba(255, 255, 153, 0.5);");
		//region.setEffect(new DropShadow(10, Color.BLACK));
		this.getChildren().add(0, region);		
	}

	@Override
	protected void mousePressed(final MouseEvent event) 
	{
		if (TransformManager.isClockShowing()) 
		{
			TransformManager.hideClockPopup();
			region.setVisible(false);
			ConfigFile.setProperty(ConfigFile.IS_CLOCK_SHOWING, "false");
		} 
		else 
		{
			TransformManager.showClockPopup();
			region.setVisible(true);
			ConfigFile.setProperty(ConfigFile.IS_CLOCK_SHOWING, "true");

		}
	}

	@Override
	protected void mouseEntered(final MouseEvent event) {
		/// TransformManager.toggleTransformPopup();

	}

	@Override
	protected void mouseExited(final MouseEvent event) {
		/// TransformManager.toggleTransformPopup();

	}

}

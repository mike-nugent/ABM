package fx.screens;

import config.ConfigFile;
import javafx.scene.paint.Color;

public class AlertSettings 
{
	private static boolean _showAlerts = true;
	private static Color _color = Color.RED;
	
	public static void initialize()
	{
		String showAlertsFlag = ConfigFile.getProperty(ConfigFile.SHOW_ALERTS);
		if(showAlertsFlag != null)
		{
			if(showAlertsFlag.equalsIgnoreCase("true"))
			{
				_showAlerts = true;
			}
			else
			{
				_showAlerts = false;
			}
		}
		
		String alertColor = ConfigFile.getProperty(ConfigFile.ALERT_TEXT_COLOR);
		if(alertColor != null)
		{
			_color = Color.web(alertColor);
		}
		else
		{
			_color = Color.RED;
		}
	}
	
	public static boolean isShowingAlerts() 
	{
		return _showAlerts;
	}

	public static void enableAlerts(boolean value) 
	{
		ConfigFile.setProperty(ConfigFile.SHOW_ALERTS, value+"");
		_showAlerts = value;
	}
	
	public static void setAlertColor(Color color)
	{
		ConfigFile.setProperty(ConfigFile.ALERT_TEXT_COLOR, color.toString()); 
		_color = color;
	}

	public static Color getAlertColor() 
	{
		return _color;
	}
}

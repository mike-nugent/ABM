package fx.screens;

import config.ConfigFile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AlertConfigScreen extends HBox
{
	ColorPicker colorPicker = new ColorPicker();

    public AlertConfigScreen()
    {
    	
    	VBox hbox = new VBox();
    	hbox.setSpacing(10);
    	hbox.setPadding(new Insets(25));
    	hbox.setAlignment(Pos.TOP_LEFT);
    	
    	
    	
    	//Show enable buttons
    	RadioButton showAlerts = new RadioButton("Show Alerts");
    	RadioButton hideAlerts = new RadioButton("Hide Alerts");
    	ToggleGroup grp = new ToggleGroup();
    	showAlerts.setOnAction(new EventHandler<ActionEvent>() 
    	{
			@Override
			public void handle(ActionEvent event) 
			{
				AlertSettings.enableAlerts(true);
			}
		});
    	hideAlerts.setOnAction(new EventHandler<ActionEvent>() 
    	{
			@Override
			public void handle(ActionEvent event)
			{
				AlertSettings.enableAlerts(false);
			}
		});
    	
    	showAlerts.setToggleGroup(grp);
    	hideAlerts.setToggleGroup(grp);
		String showAlertsFlag = ConfigFile.getProperty(ConfigFile.SHOW_ALERTS);
      	showAlerts.setSelected(true);

		if(showAlertsFlag != null)
		{
			if(!showAlertsFlag.equalsIgnoreCase("true"))
			{
				hideAlerts.setSelected(true);
			}
		}

		
    	VBox btns = new VBox(10, showAlerts, hideAlerts);
    	
    	
    	
    	//Set color of alert
    	Label colorLabel = new Label("Alert Text Color");
    	colorPicker.setValue(AlertSettings.getAlertColor());
    	colorPicker.setOnAction(new EventHandler<ActionEvent>() 
    	{
			@Override
			public void handle(ActionEvent event) 
			{
				AlertSettings.setAlertColor(colorPicker.getValue());
			}
		});
    	HBox colorBox = new HBox(10, colorPicker, colorLabel);
    	colorBox.setAlignment(Pos.CENTER_LEFT);
    	
    	
    	
    	
    	//Show test alert button
    	Button testAlert = new Button("Test Alert");
    	testAlert.setOnAction(new EventHandler<ActionEvent>() 
    	{
			@Override
			public void handle(ActionEvent event) 
			{
				AlertScreen.showAlert("This is a test alert");
			}
		});
    	
    	
    	
    	hbox.getChildren().addAll(btns, colorBox, testAlert);  	
    	this.getChildren().addAll(hbox);
      
    }
}

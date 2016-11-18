package main;

import gameinfo.IconLoader;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ScreenButton extends StackPane
{
	public ScreenButton(String iconName, int height)
	{
		Image image = IconLoader.loadFxImage(iconName, height);
		final ImageView imageView = new ImageView(image);

		this.getChildren().add(imageView);
		imageView.setEffect(new DropShadow(10, Color.DARKGRAY));

		this.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent event) 
			{
				mousePressed(event);
				imageView.setEffect(new DropShadow(10, Color.RED)); 


			}

		});

		this.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent evt) 
			{
				imageView.setEffect(new DropShadow(10, Color.GREEN));
				mouseEntered(evt);
			}


		});

		this.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent evt) 
			{
				imageView.setEffect(new DropShadow(10, Color.DARKGRAY));
				mouseExited(evt);
			}

		});
	}
	

	protected void mousePressed(MouseEvent event) 
	{
		
	}
	
	protected  void mouseEntered(MouseEvent event)
	{
		
	}
	protected  void mouseExited(MouseEvent event) 
	{
		
	}
	
}

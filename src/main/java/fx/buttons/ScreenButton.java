package fx.buttons;

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
	ScreenButton me;
	public ScreenButton(String iconName, int height)
	{
		me = this;
		Image image = IconLoader.loadFxImage(iconName, height);
		final ImageView imageView = new ImageView(image);

		this.getChildren().add(imageView);
		me.setEffect(new DropShadow(10, Color.DARKGRAY));

		this.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent event) 
			{
				mousePressed(event);
				//me.setEffect(new DropShadow(10, Color.RED)); 


			}

		});

		this.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent evt) 
			{
				me.setEffect(new DropShadow(15, Color.web("#ffffb3")));
				mouseEntered(evt);
			}


		});

		this.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent evt) 
			{
				me.setEffect(new DropShadow(10, Color.DARKGRAY));
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

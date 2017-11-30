package fx.screens;

import java.util.HashMap;
import java.util.Map;

import artifact.AbyssArtifact;
import artifact.ArtifactData;
import artifact.ArtifactLocationFX;
import config.ConfigFile;
import fx.buttons.MoveCursor;
import gameinfo.IconLoader;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.ABMStage;
import main.DisplayManager;

public class ArtifactPopupPage extends Stage 
{
	private double xOffset = 0;
	private double yOffset = 0;
	Stage me;

	static Map<AbyssArtifact, ArtifactLocationFX> artieMap = new HashMap<AbyssArtifact, ArtifactLocationFX>();


	private final MoveCursor _moveCursor = new MoveCursor();
	final ImageView _closeBtn = new ImageView(IconLoader.loadFxImage("close.png", 25));

	@SuppressWarnings("unchecked")
	public ArtifactPopupPage()
	{
		me = this;
		this.setAlwaysOnTop(true);
		this.initStyle(StageStyle.TRANSPARENT);

		_closeBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(final MouseEvent event)
            {
            	DisplayManager.toggleArtifactPopup();
            }

        });
 
		final HBox barNBtn = new HBox();
		barNBtn.setSpacing(10);
		barNBtn.getChildren().addAll(_moveCursor, _closeBtn);
		HBox.setHgrow(barNBtn, Priority.ALWAYS); // Give stack any extra
		barNBtn.setAlignment(Pos.BOTTOM_RIGHT);


		final VBox displayWrapper = new VBox();
		displayWrapper.setSpacing(10);
		positionListener(displayWrapper);


		ImageView background = new ImageView(IconLoader.loadFxImage("reshanta.png", 800));
		displayWrapper.setStyle("-fx-background-color: null;");
		displayWrapper.setAlignment(Pos.TOP_CENTER);
		
		Pane artBoard = new Pane();
		artBoard.getChildren().addAll(background);
		displayWrapper.getChildren().addAll(barNBtn, artBoard);

		
		//Setup logic ------
		ArtifactLocationFX krotonMiren = new ArtifactLocationFX(AbyssArtifact.north);
		ArtifactLocationFX mirenKysis = new ArtifactLocationFX(AbyssArtifact.right);
		ArtifactLocationFX kysisKroton = new ArtifactLocationFX(AbyssArtifact.left);
		ArtifactLocationFX hellfire = new ArtifactLocationFX(AbyssArtifact.hellfire);
		
		
		
		artieMap.put(AbyssArtifact.north, krotonMiren);
		artieMap.put(AbyssArtifact.right, mirenKysis);
		artieMap.put(AbyssArtifact.left, kysisKroton);
		artieMap.put(AbyssArtifact.hellfire, hellfire);

		artBoard.getChildren().addAll(krotonMiren, mirenKysis, kysisKroton, hellfire);

		
		krotonMiren.relocate(250, 130);
		mirenKysis.relocate(600,385);  
		kysisKroton.relocate(185,545);  
		hellfire.relocate(350,352);  

		
	
		
		final Scene scene = new Scene(displayWrapper);
		scene.setFill(Color.TRANSPARENT);
		this.setScene(scene);

		final String stageLocation = ConfigFile.getProperty(ConfigFile.ARTI_LOCATION_PROPERTY);
		if (stageLocation != null) {
			final String[] XandY = stageLocation.split(",");
			final double winx = Double.parseDouble(XandY[0]);
			final double winy = Double.parseDouble(XandY[1]);

			me.setX(winx);
			me.setY(winy);
		}
	}
	
	public static void artifactWasCaptured(ArtifactData info) 
	{
		AbyssArtifact key = info.location;
		if(artieMap.containsKey(key))
		{
			ArtifactLocationFX panel = artieMap.get(key);
			panel.artifactCaptured(info);
		} 
		else 
		{
			System.out.println("error, no key for " + key);
		}
	}
	


	public void positionListener(final Pane pane) {
		pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (ABMStage.getWindowLock()) {
					return;
				}

				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (ABMStage.getWindowLock()) {
					return;
				}
				me.setX(event.getScreenX() - xOffset);
				me.setY(event.getScreenY() - yOffset);
			}
		});
		pane.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (ABMStage.getWindowLock()) {
					return;
				}

				// When we drag the screen around and then release the mouse,
				// save the screen location to the file.
				// So when we open the program again, it remembers where we
				// positioned it.
				ConfigFile.setProperty(ConfigFile.ARTI_LOCATION_PROPERTY, me.getX() + "," + me.getY());
			}
		});
	}
}

package fx.screens;

import config.ConfigFile;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.timer.FxClock;
import javafx.timer.FxClockController;
import main.ASDMStage;

public class ClockPopupPage extends PopupStage 
{
	private double xOffset = 0;
	private double yOffset = 0;

	public ClockPopupPage() {
		super("Clock Stage");
		final Pane root = new Pane();
		root.setStyle("-fx-background-color: null;");
		root.setPadding(new Insets(10));
		
		this.setAlwaysOnTop(true);
		FxClock clock = new FxClock();
        final FxClockController clockController = new FxClockController(clock);
        HBox box = new HBox();
        box.getChildren().addAll(clock, clockController);

		root.getChildren().addAll(box);
		positionListener(root);

		//this.setWidth(200);
		//this.setHeight(100);
		
		

		final String stageLocation = ConfigFile.getProperty(ConfigFile.CLOCK_LOCATION_PROPERTY);
		if (stageLocation != null) {
			final String[] XandY = stageLocation.split(",");
			final double winx = Double.parseDouble(XandY[0]);
			final double winy = Double.parseDouble(XandY[1]);

			me.setX(winx);
			me.setY(winy);
		}

		this.initStyle(StageStyle.TRANSPARENT);
        final Scene scene = new Scene(root, 200, 100);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
	}


	public void positionListener(Pane root) {
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (ASDMStage.getWindowLock()) {
					return;
				}

				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (ASDMStage.getWindowLock()) {
					return;
				}
				me.setX(event.getScreenX() - xOffset);
				me.setY(event.getScreenY() - yOffset);
			}
		});
		root.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (ASDMStage.getWindowLock()) {
					return;
				}

				// When we drag the screen around and then release the mouse,
				// save the screen location to the file.
				// So when we open the program again, it remembers where we
				// positioned it.
				ConfigFile.setProperty(ConfigFile.CLOCK_LOCATION_PROPERTY, me.getX() + "," + me.getY());
			}
		});
	}

}

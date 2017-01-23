package fx.screens;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import config.ConfigFile;
import fx.buttons.MoveCursor;
import gameinfo.IconLoader;
import gameinfo.PlayerData;
import gameinfo.Race;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.ASDMStage;
import main.DisplayManager;
import skins.Skins;
import sounds.SoundManager;

public class PvPPopupPage extends Stage 
{

	private double xOffset = 0;
	private double yOffset = 0;
	Stage me;

	VBox transformStage = new VBox();
	VBox elyosStage = new VBox();
	VBox asmoStage = new VBox();

	ScrollPane elyosScroller = makeScroller();
	ScrollPane asmoScroller = makeScroller();

	Label asmoXforms = new Label("Your Kills");
	Label elyXforms = new Label("Enemy Kills");

	private final MoveCursor _moveCursor = new MoveCursor();

	@SuppressWarnings("unchecked")
	public PvPPopupPage()
	{
		asmoXforms.setTooltip(new Tooltip("There are no asmodians with xforms active")); // f
		me = this;
		this.setAlwaysOnTop(true);
		this.initStyle(StageStyle.TRANSPARENT);

		elyosScroller.setContent(elyosStage);
		asmoScroller.setContent(asmoStage);

		transformStage.setPadding(new Insets(0, 6, 0, 0));
		elyosStage.setPadding(new Insets(0, 6, 0, 0));
		asmoStage.setPadding(new Insets(0, 6, 0, 0));

		transformStage.getChildren().addAll(elyosScroller, asmoScroller);

		positionListener(_moveCursor);


		final HBox counters = new HBox();
		counters.setSpacing(20);

		final HBox barNBtn = new HBox();
		barNBtn.setSpacing(10);
		// TODO - consider close btn
		final ImageView vw = new ImageView(IconLoader.loadFxImage("close.png", 25));

		barNBtn.getChildren().addAll(_moveCursor);
		HBox.setHgrow(barNBtn, Priority.ALWAYS); // Give stack any extra
		barNBtn.setAlignment(Pos.BOTTOM_RIGHT);

		counters.getChildren().addAll(asmoXforms, elyXforms, barNBtn);

		updateLabel(asmoXforms, Color.DODGERBLUE, 15, "bold");
		updateLabel(elyXforms, Color.LIMEGREEN, 15, "bold");

		final VBox displayWrapper = new VBox(counters, transformStage);
		displayWrapper.setSpacing(15);
		displayWrapper.setStyle("-fx-background-color: null;");
		displayWrapper.setAlignment(Pos.TOP_CENTER);
		final Scene scene = new Scene(displayWrapper);
		scene.setFill(Color.TRANSPARENT);
		this.setScene(scene);

		final String stageLocation = ConfigFile.getProperty(ConfigFile.PVP_LOCATION_PROPERTY);
		if (stageLocation != null) {
			final String[] XandY = stageLocation.split(",");
			final double winx = Double.parseDouble(XandY[0]);
			final double winy = Double.parseDouble(XandY[1]);

			me.setX(winx);
			me.setY(winy);
		}
	}
	
    private void updateLabel(final Label label, final Color c, final int size, final String font_weight)
    {
        label.setTextFill(c);
        label.setFont(new Font(size));
        label.setStyle("-fx-font-weight: " + font_weight + ";");
    }

	private ScrollPane makeScroller() {
		final ScrollPane pane = new ScrollPane();
		pane.setPadding(new Insets(10));
		pane.getStylesheets().add(Skins.get("CustomScrollPane.css"));
		pane.setHbarPolicy(ScrollBarPolicy.NEVER);
		pane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		pane.setMaxHeight(300);
		pane.setMinHeight(100);
		pane.setMinWidth(150);
		pane.setPrefWidth(280);

		pane.setStyle("-fx-border-color: rgb(100,100,100);");
		return pane;
	}

	public void positionListener(final Pane pane) {
		pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (ASDMStage.getWindowLock()) {
					return;
				}

				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (ASDMStage.getWindowLock()) {
					return;
				}
				me.setX(event.getScreenX() - xOffset);
				me.setY(event.getScreenY() - yOffset);
			}
		});
		pane.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (ASDMStage.getWindowLock()) {
					return;
				}

				// When we drag the screen around and then release the mouse,
				// save the screen location to the file.
				// So when we open the program again, it remembers where we
				// positioned it.
				ConfigFile.setProperty(ConfigFile.PVP_LOCATION_PROPERTY, me.getX() + "," + me.getY());
			}
		});
	}
}

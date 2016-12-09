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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
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
import main.TransformManager;

public class XformPopupStage extends Stage
{
	private double xOffset = 0;
	private double yOffset = 0;
	Stage me;
	
    Map<PlayerData, TransformBarFX>               activeTransforms   = new HashMap<PlayerData, TransformBarFX>();
    private final Map<PlayerData, TransformBarFX> cooldownTransforms = new HashMap<PlayerData, TransformBarFX>();
    VBox transformStage = new VBox();
    VBox elyosStage = new VBox();
    VBox asmoStage = new VBox();

    ScrollPane elyosScroller = makeScroller();
    ScrollPane asmoScroller = makeScroller();
    
    Label asmoXforms = new Label("Asmodians ( 0 )");
    Label elyXforms = new Label("Elyos ( 0 )");

    private final MoveCursor  _moveCursor    = new MoveCursor();


    @SuppressWarnings("unchecked")
    public XformPopupStage()
    {
    	asmoXforms.setTooltip(new Tooltip("There are no asmodians with xforms active"));
    	me = this;
		this.setAlwaysOnTop(true);
		this.initStyle(StageStyle.TRANSPARENT);
		
		elyosScroller.setContent(elyosStage);
		asmoScroller.setContent(asmoStage);
		
		transformStage.setPadding(new Insets(0,20,0,0));
		elyosStage.setPadding(new Insets(0,20,0,0));		
		asmoStage.setPadding(new Insets(0,20,0,0));		

		transformStage.getChildren().addAll(elyosScroller, asmoScroller);
		
    	positionListener(_moveCursor);
        
        
        new AnimationTimer()
        {
            @Override
            public void handle(final long now)
            {
                activeXformEffectTimer();
                cooldownXformEffectTimer();
            }

        }.start();
        
        for(int i = 0; i < 0; i++)
        {
        	addNewXform(PlayerData.generateRandom());
        }
        
        HBox counters = new HBox();
        counters.setSpacing(20);
        counters.getChildren().addAll(asmoXforms, elyXforms,_moveCursor);
 
        updateLabel(asmoXforms, Color.DODGERBLUE, 15, "bold");
        updateLabel(elyXforms, Color.LIMEGREEN, 15, "bold");

        VBox displayWrapper = new VBox(counters, transformStage);
        displayWrapper.setSpacing(15);
        displayWrapper.setStyle("-fx-background-color: null;");
        displayWrapper.setAlignment(Pos.CENTER_RIGHT);
        final Scene scene = new Scene(displayWrapper);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
        
		final String stageLocation = ConfigFile.getProperty(ConfigFile.XFORM_LOCATION_PROPERTY);
		if (stageLocation != null) {
			final String[] XandY = stageLocation.split(",");
			final double winx = Double.parseDouble(XandY[0]);
			final double winy = Double.parseDouble(XandY[1]);

			me.setX(winx);
			me.setY(winy);
		}
    }
    
	private void updateLabel(Label label, Color c, int size, String font_weight) 
    {
    	label.setTextFill(c);
    	label.setFont(new Font(size));
    	label.setStyle("-fx-font-weight: "+font_weight+";");
	}
    
    private ScrollPane makeScroller() 
    {
    	ScrollPane pane = new ScrollPane();
		pane.getStylesheets().add("skins/CustomScrollPane.css");
        pane.setHbarPolicy(ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        pane.setMaxHeight(300);
        pane.setMinHeight(100);
        pane.setMinWidth(200);
        pane.setStyle("-fx-border-color: rgb(100,100,100);");
        return pane;
	}

	public void positionListener(Pane pane) {
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
				ConfigFile.setProperty(ConfigFile.XFORM_LOCATION_PROPERTY, me.getX() + "," + me.getY());
			}
		});
	} 
    

    private void cooldownXformEffectTimer()
    {
        try
        {
            PlayerData removeFlag = null;
            final Date currentTime = new Date();
            for (final PlayerData key : cooldownTransforms.keySet())
            {
                final TransformBarFX bar = cooldownTransforms.get(key);
                final long timeLeft = bar.updateTimerForCooldown(currentTime);
                if (timeLeft <= 0)
                {
                    removeFlag = key;
                }
            }

            if (removeFlag != null)
            {
                TransformManager.removeCooldown(removeFlag);
            }

        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void activeXformEffectTimer()
    {
        try
        {
            PlayerData removeFlag = null;
            final Date currentTime = new Date();
            for (final PlayerData key : activeTransforms.keySet())
            {
                final TransformBarFX bar = activeTransforms.get(key);
                final long timeLeft = bar.updateTimerForActive(currentTime);
                if (timeLeft <= 0)
                {
                    removeFlag = key;
                }
            }

            if (removeFlag != null)
            {
                TransformManager.transitionFromActiveToCooldown(removeFlag);
            }

        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addNewXform(final PlayerData newXform)
    {

        final TransformBarFX player = new TransformBarFX();
        player.setInfo(newXform);
        activeTransforms.put(newXform, player);

        // Check to see if we should put back up a no-data message
        if(Race.Asmodian.equals(newXform.race))
        {
        	asmoStage.getChildren().add(player);
        	asmoXforms.setText("Asmodians ( " +asmoStage.getChildren().size() + " )");
        }
        else
        {
        	elyosStage.getChildren().add(player);
        	elyXforms.setText("Elyos ( " + elyosStage.getChildren().size() + " )");
        }
    }

    public void transitionFromActiveToCooldown(final PlayerData data)
    {
        final TransformBarFX bar = activeTransforms.remove(data);
        bar.goInactiveColors();

        cooldownTransforms.put(data, bar);

    }



    public void removeCooldown(final PlayerData data)
    {
        final TransformBarFX bar = cooldownTransforms.remove(data);
        
        if(Race.Asmodian.equals(data.race))
        {
        	asmoStage.getChildren().remove(bar);
        	asmoXforms.setText("Asmodians ( " +asmoStage.getChildren().size() + " )");

        }
        else
        {
        	elyosStage.getChildren().remove(bar);
        	elyXforms.setText("Elyos ( " + elyosStage.getChildren().size() + " )");
        }
    }
}

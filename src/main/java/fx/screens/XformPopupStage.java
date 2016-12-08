package fx.screens;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gameinfo.PlayerData;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.TransformManager;

public class XformPopupStage extends Stage
{
    Map<PlayerData, TransformBarFX>               activeTransforms   = new HashMap<PlayerData, TransformBarFX>();
    private final Map<PlayerData, TransformBarFX> cooldownTransforms = new HashMap<PlayerData, TransformBarFX>();
    VBox transformStage = new VBox();
    ScrollPane pane = new ScrollPane();


    @SuppressWarnings("unchecked")
    public XformPopupStage()
    {

		this.setAlwaysOnTop(true);
		this.initStyle(StageStyle.TRANSPARENT);
		
    	final Pane root = new Pane();
		root.setStyle("-fx-background-color: null;");
		pane.setStyle("-fx-control-inner-background:null;");

		root.setPadding(new Insets(10));
		root.getChildren().add(transformStage);
		

		
        pane.setContent(root);
        pane.setHbarPolicy(ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        pane.setMaxHeight(600);
        new AnimationTimer()
        {
            @Override
            public void handle(final long now)
            {
                activeXformEffectTimer();
                cooldownXformEffectTimer();
            }

        }.start();
        
        for(int i = 0; i < 100; i++)
        {
        	addNewXform(PlayerData.generateRandom());
        }
        
        
        final Scene scene = new Scene(pane);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
        
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
        transformStage.getChildren().add(player);
    }

    public void transitionFromActiveToCooldown(final PlayerData data)
    {
        final TransformBarFX bar = activeTransforms.remove(data);

        cooldownTransforms.put(data, bar);

    }



    public void removeCooldown(final PlayerData data)
    {
        final TransformBarFX bar = cooldownTransforms.remove(data);
        transformStage.getChildren().remove(bar);

    }
}

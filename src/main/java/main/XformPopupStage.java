package main;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gameinfo.PlayerData;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class XformPopupStage extends PopupStage
{
    private final TabPane                        _tabPane;
    Map<String, Tab>                             currentTabs        = new HashMap<String, Tab>();
    Map<PlayerData, TransformBar2>               activeTransforms   = new HashMap<PlayerData, TransformBar2>();
    private final Map<PlayerData, TransformBar2> cooldownTransforms = new HashMap<PlayerData, TransformBar2>();
    Label                                        noInfoTitle        = new Label("No transforms detected!");
    Label                                        noInfoDescription  = new Label(
            "Information will show on this window\nwhen players transform into Guardian Generals.");
    VBox                                         infoBox            = new VBox();

    @SuppressWarnings("unchecked")
    public XformPopupStage()
    {
        super("Transforms Detected");

        _tabPane = new TabPane();
        stage.getChildren().add(_tabPane);

        noInfoTitle.setStyle("-fx-font-size: 18px;" + "-fx-font-weight: bold;");
        noInfoDescription.setStyle("-fx-font-size: 16px;");

        infoBox.getChildren().addAll(noInfoTitle, noInfoDescription);
        stage.getChildren().add(infoBox);

        checkUpdateDisplay();
        new AnimationTimer()
        {
            @Override
            public void handle(final long now)
            {
                activeXformEffectTimer();
                cooldownXformEffectTimer();
            }

        }.start();
    }

    private void cooldownXformEffectTimer()
    {
        try
        {
            PlayerData removeFlag = null;
            final Date currentTime = new Date();
            for (final PlayerData key : cooldownTransforms.keySet())
            {
                final TransformBar2 bar = cooldownTransforms.get(key);
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
                final TransformBar2 bar = activeTransforms.get(key);
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

    public Tab getOrCreateTab(final String nameKey)
    {

        Tab currentTab = currentTabs.get(nameKey);
        if (currentTab == null)
        {
            final Tab tab = new Tab(nameKey);
            tab.setStyle("-fx-font-size: 18px;");
            tab.setClosable(false);
            currentTabs.put(nameKey, tab);
            _tabPane.getTabs().add(tab);

            final VBox vbox = new VBox();
            vbox.setPadding(new Insets(10));
            vbox.setAlignment(Pos.TOP_LEFT);

            final ScrollPane sp = new ScrollPane();
            sp.setContent(vbox);
            tab.setContent(sp);

            currentTab = tab;
        }

        return currentTab;
    }

    public void addNewXform(final PlayerData newXform)
    {
        final String key = newXform.server.getServerString() + "-" + newXform.race.getAcyonym();

        final Tab currentTab = getOrCreateTab(key);

        final VBox box = (VBox) ((ScrollPane) currentTab.getContent()).getContent();
        final TransformBar2 player = new TransformBar2();
        player.setInfo(newXform);
        activeTransforms.put(newXform, player);

        // Check to see if we should put back up a no-data message
        checkUpdateDisplay();
        box.getChildren().add(player);

        currentTab.setText(key + " (" + box.getChildren().size() + ") ");
    }

    public void transitionFromActiveToCooldown(final PlayerData data)
    {
        final TransformBar2 bar = activeTransforms.remove(data);
        final String key = data.server.getServerString() + "-" + data.race.getAcyonym();

        final Tab t = currentTabs.get(key);
        final VBox box = getContent(t);
        box.getChildren().remove(bar);

        if (box.getChildren().size() == 0)
        {
            _tabPane.getTabs().remove(t);
            currentTabs.remove(key);
        }
        else
        {
            t.setText(key + " (" + box.getChildren().size() + ") ");

        }

        cooldownTransforms.put(data, bar);
        final Tab cooldownTab = getOrCreateTab("Cooldowns");
        cooldownTab.setText("Cooldowns (" + box.getChildren().size() + ") ");

        final VBox cdbox = getContent(cooldownTab);
        cdbox.getChildren().add(bar);

        checkUpdateDisplay();

    }

    private VBox getContent(final Tab tab)
    {
        return (VBox) ((ScrollPane) tab.getContent()).getContent();
    }

    private void checkUpdateDisplay()
    {
        if (_tabPane.getTabs().size() <= 0)
        {
            infoBox.setVisible(true);
            this.setTitle("No Transforms Detected");
        }
        else
        {
            infoBox.setVisible(false);
            this.setTitle("Transforms Detected");
        }
    }

    public void removeCooldown(final PlayerData data)
    {
        final TransformBar2 bar = cooldownTransforms.remove(data);
        final Tab tab = currentTabs.get("Cooldowns");
        final VBox box = getContent(tab);
        box.getChildren().remove(bar);

        if (box.getChildren().size() == 0)
        {
            _tabPane.getTabs().remove(tab);
            currentTabs.remove("Cooldowns");
        }
        else
        {
            tab.setText("Cooldowns (" + box.getChildren().size() + ") ");
        }

        checkUpdateDisplay();
    }
}

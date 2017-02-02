package artifact;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import gameinfo.IconLoader;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import utils.Times;

public class ArtifactLocationFX extends VBox
{
    private final Label factionLabel;
    private final Label legionLabl;
    private final Label vulnLabel;
    private final Label triggerLabel;
    private final Label noData;

    private final VBox          activeData = new VBox();
    private final AbyssArtifact _artie;
    private ArtifactData        _lastCaptureData;

    public ArtifactLocationFX(final AbyssArtifact artie)
    {
        activeData.setPadding(new Insets(10));
        activeData.setSpacing(3);
        activeData.setAlignment(Pos.CENTER);
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: #333333;");
        this.setMinWidth(200);

        _artie = artie;
        factionLabel = makeLabel("", Color.CYAN, 9);
        legionLabl = makeLabel("", Color.CYAN, 15);
        vulnLabel = makeLabel("", Color.RED, 20, new ImageView(IconLoader.loadFxImage("artifact-captain.png", 32)));
        triggerLabel = makeLabel("", Color.LIGHTGREEN, 20,
                new ImageView(IconLoader.loadFxImage("artifact-fire.png", 32)));
        noData = makeLabel("no data", Color.GRAY, 20);
        noData.setAlignment(Pos.CENTER);
        activeData.getChildren().addAll(factionLabel, legionLabl, vulnLabel, triggerLabel);

        deactivateInfo();

        new AnimationTimer()
        {
            @Override
            public void handle(final long now)
            {
                artieCountdownTimer();
            }

        }.start();
    }

    protected void artieCountdownTimer()
    {
        try
        {
            if (_lastCaptureData != null)
            {
                final Date currentTime = new Date();
                final long difference = currentTime.getTime() - _lastCaptureData.date.getTime();
                long vulnerableCooldown = _lastCaptureData.owner.getCaptainSpawnCooldown() - difference;
                long useableCooldown = _artie.getActivationCooldown() - difference;

                final double scale = ((double) vulnerableCooldown
                        / (double) _lastCaptureData.owner.getCaptainSpawnCooldown());
                final double midLine = 200;
                final int edge = (int) (255 - midLine);
                final int gScale = (int) (midLine * scale);
                final int rScale = (int) (midLine - (midLine * scale));

                int R = edge / 2 + rScale;
                int G = edge / 2 + gScale;
                int B = 0;

                if (R < 0 || R > 255)
                {
                    R = 0;
                }
                if (G < 0 || G > 255)
                {
                    G = 0;
                }
                if (B < 0 || B > 255)
                {
                    B = 0;
                }

                if (vulnerableCooldown < 0)
                {
                    vulnerableCooldown = 0;
                    vulnLabel.setTextFill(Color.RED);
                    vulnLabel.setText("Vulnerable");

                }
                else
                {
                    final String vulnTimer = getMinSec(vulnerableCooldown);
                    vulnLabel.setTextFill(Color.rgb(R, G, B));
                    vulnLabel.setText(vulnTimer);
                }

                if (useableCooldown < 0)
                {
                    useableCooldown = 0;
                    triggerLabel.setTextFill(Color.LIGHTGREEN);
                    triggerLabel.setText("Ready");
                }
                else
                {
                    final String vulnTimer = getMinSec(useableCooldown);
                    triggerLabel.setTextFill(Color.ORANGE);
                    triggerLabel.setText(vulnTimer);
                }

                // only wait max of 1h on data. anything older is probably wrong
                if (difference > Times.ONE_HOUR)
                {
                    _lastCaptureData = null;
                }

            }
            else
            {
                deactivateInfo();
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    public AbyssArtifact getArtie()
    {
        return _artie;
    }

    public void artifactCaptured(final ArtifactData info)
    {
        _lastCaptureData = info;
        if (info.owner.equals(ArtifactOwner.Asmodian))
        {
            factionLabel.setTextFill(Color.CYAN);
            legionLabl.setTextFill(Color.CYAN);
        }
        else if (info.owner.equals(ArtifactOwner.Elyos))
        {
            factionLabel.setTextFill(Color.LIGHTGREEN);
            legionLabl.setTextFill(Color.LIGHTGREEN);
        }
        else
        {
            factionLabel.setTextFill(Color.PINK);
            legionLabl.setTextFill(Color.PINK);
            triggerLabel.setVisible(false);
        }

        factionLabel.setText(info.owner.name());
        legionLabl.setText(info.conqueringLegion);

        activateInfo();
    }

    public synchronized void activateInfo()
    {
        if (this.getChildren().contains(noData))
        {
            this.getChildren().remove(noData);
        }

        if (!this.getChildren().contains(activeData))
        {
            this.getChildren().add(activeData);
        }
    }

    public synchronized void deactivateInfo()
    {
        if (this.getChildren().contains(activeData))
        {
            this.getChildren().remove(activeData);
        }

        if (!this.getChildren().contains(noData))
        {
            this.getChildren().add(noData);
        }
    }

    private Label makeLabel(final String txt, final Color color, final int size, final Node icon)
    {
        final Label label = new Label(txt, icon);
        label.setTextFill(color);
        label.setFont(Font.font(size));
        return label;
    }

    private Label makeLabel(final String txt, final Color color, final int size)
    {
        final Label label = new Label(txt);
        label.setTextFill(color);
        label.setFont(Font.font(size));
        return label;
    }

    public String getMinSec(final long millisec)
    {
        final String ms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millisec)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisec)),
                TimeUnit.MILLISECONDS.toSeconds(millisec)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisec)));
        return ms;
    }

}

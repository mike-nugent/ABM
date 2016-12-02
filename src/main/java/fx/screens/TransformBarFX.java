package fx.screens;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import gameinfo.IconLoader;
import gameinfo.PlayerData;
import gameinfo.Rank;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class TransformBarFX extends HBox
{
    private static final long TEN_MINUTES = 600000;
    public static final long  TWO_HOURS   = 7200000;
    private String            _name;
    Label                     _timeTxt;
    private Date              _transformStartTime;
    private Rank              _rank;

    public TransformBarFX()
    {
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER_LEFT);
    }

    public void setInfo(final PlayerData newXform)
    {
        final ImageView icon = new ImageView(IconLoader.loadFxImage(newXform.clazz.getIconName()));
        _name = newXform.name;
        _transformStartTime = newXform.dateTime;
        _timeTxt = getTimeLabel();
        _rank = newXform.rank;
        final Label rn = new Label(_name + " - " + _rank.getRankTitle());
        rn.setFont(Font.font(25));
        this.getChildren().addAll(icon, _timeTxt, rn);
    }

    private Label getTimeLabel()
    {
        final Label l = new Label("10:00");
        return l;
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

    public String getHrMinSec(final long millisec)
    {
        final String ms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisec),
                TimeUnit.MILLISECONDS.toMinutes(millisec)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisec)),
                TimeUnit.MILLISECONDS.toSeconds(millisec)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisec)));
        return ms;
    }

    /**
     * returns time left in ms;
     *
     * @param currentTime
     *
     * @return
     */
    public long updateTimerForActive(final Date currentTime)
    {
        final long difference = currentTime.getTime() - _transformStartTime.getTime();
        long countDown = TEN_MINUTES - difference;
        if (countDown < 0)
        {
            countDown = 0;
        }

        final double scale = ((double) countDown / (double) TEN_MINUTES);
        final double midLine = 150;
        final int edge = (int) (255 - midLine);
        final int gScale = (int) (midLine * scale);
        final int rScale = (int) (midLine - (midLine * scale));

        final String diffString = getMinSec(countDown);
        _timeTxt.setText(diffString);

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

        _timeTxt.setStyle("-fx-font-weight: bold;" + "-fx-text-fill: rgba(" + R + "," + G + "," + B + ",1);"
                + "-fx-font-size: 30px;");

        return countDown;
    }

    public long updateTimerForCooldown(final Date currentTime)
    {
        final long difference = currentTime.getTime() - _transformStartTime.getTime();
        final long cooldownLeft = TWO_HOURS - difference;
        final String cooldown = getHrMinSec(cooldownLeft);
        _timeTxt.setText(cooldown);

        return cooldownLeft;
    }
}

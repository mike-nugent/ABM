package javafx.timer;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import utils.Times;

public class FxClock extends Pane
{
    final Label _minSecTxt = new Label("00:00");
    final Label _msTxt     = new Label("00");

    boolean _isPlaying = false;

    protected long _startTime  = 0;
    long           _targetTime = Times.TEN_MIUTES;

    AnimationTimer    animationtimer;
    protected boolean countUpEnabled = true; // DODO - configure this

    private static FxClock clockInstance;

    public static void startClock(final String mode, final long target)
    {
        if (clockInstance == null)
        {
            return;
        }

        clockInstance.stopClock();
        clockInstance.resetClock();

        if (mode.equals("Count Down From"))
        {
            clockInstance.countDownFrom(target);
        }
        else if (mode.equals("Count Up To"))
        {
            clockInstance.countUpTo(target);
        }
    }

    public FxClock()
    {
        clockInstance = this;
        this.setMouseTransparent(true);
        // this.setEffect(new DropShadow(10, Color.BLACK));

        _minSecTxt.setStyle("-fx-font-size: 85px;" + "-fx-font-weight: bold;");
        _msTxt.setStyle("-fx-font-size: 30px;" + "-fx-font-weight: bold;");

        _msTxt.setLayoutX(220);
        _msTxt.setLayoutY(20);
        _minSecTxt.setTextFill(Color.DIMGREY);
        _msTxt.setTextFill(Color.DIMGREY);

        this.getChildren().addAll(_minSecTxt, _msTxt);
    }

    public void resetClock()
    {
        _isPlaying = false;
        if (animationtimer != null)
        {
            animationtimer.stop();
            animationtimer = null;
            _minSecTxt.setText("00:00");
            _msTxt.setText("00");

            _minSecTxt.setTextFill(Color.DIMGREY);
            _msTxt.setTextFill(Color.DIMGREY);
        }
    }

    public boolean isPlaying()
    {
        return _isPlaying;
    }

    public void stopClock()
    {
        _isPlaying = false;
        if (animationtimer != null)
        {
            animationtimer.stop();
        }
    }

    public void startClock()
    {
        _isPlaying = true;
        if (animationtimer == null)
        {
            _startTime = System.currentTimeMillis();

            animationtimer = new AnimationTimer()
            {
                @Override
                public void handle(final long now)
                {

                    // Count up
                    if (countUpEnabled)
                    {
                        countUp(now);
                    }
                    else
                    {
                        countDown(now);
                    }

                }

            };
        }

        animationtimer.start();
    }

    private void countUpTo(final long target)
    {
        _targetTime = target;
        countUpEnabled = true;
        startClock();
    }

    private void countDownFrom(final long target)
    {
        _targetTime = target;
        countUpEnabled = false;
        startClock();
    }

    private void countUp(final long now)
    {
        final long diff = System.currentTimeMillis() - _startTime;
        String milliSec = diff + "";
        if (milliSec.length() >= 3)
        {
            milliSec = milliSec.substring(milliSec.length() - 3, milliSec.length() - 1);
        }

        final String time = Times.getMinSec(diff);
        final Color c = getColorBasedOnTimeCountUp(diff);

        _minSecTxt.setTextFill(c);
        _msTxt.setTextFill(c);

        _minSecTxt.setText(time);
        _msTxt.setText(milliSec);

        if (diff >= _targetTime)
        {
            stopClock();
        }
    }

    private void countDown(final long now)
    {
        // Count down
        final long diff = System.currentTimeMillis() - _startTime;
        long countdown = _targetTime - diff;

        if (countdown <= 0)
        {
            countdown = 0;
        }
        String milliSec = countdown + "";

        if (milliSec.length() >= 3)
        {
            milliSec = milliSec.substring(milliSec.length() - 3, milliSec.length() - 1);
        }
        else
        {
            milliSec = "0";
        }

        final Color c = getColorBasedOnTimeCountDown(countdown);
        final String time = Times.getMinSec(countdown);

        _minSecTxt.setText(time);
        _msTxt.setText(milliSec);
        _minSecTxt.setTextFill(c);
        _msTxt.setTextFill(c);

        if (countdown <= 0)
        {
            stopClock();
        }
    }

    protected Color getColorBasedOnTimeCountUp(final long diff)
    {
        final double scaledTo = _targetTime;
        final double percentLeft = scaledTo - diff;

        final double scale = (percentLeft / scaledTo);
        final double midLine = 150;
        final int edge = (int) (255 - midLine);
        final int gScale = (int) (midLine * scale);
        final int rScale = (int) (midLine - (midLine * scale));

        int R = edge / 2 + rScale;
        int G = edge / 2 + gScale;
        int B = 0;

        if (R < 0 || R > 255)
        {
            R = 255;
        }
        if (G < 0 || G > 255)
        {
            G = 0;
        }
        if (B < 0 || B > 255)
        {
            B = 0;
        }

        return Color.rgb(R, G, B);
    }

    protected Color getColorBasedOnTimeCountDown(final long diff)
    {

        final double scale = ((double) diff / (double) _targetTime);
        final double midLine = 150;
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

        return Color.rgb(R, G, B);
    }

}

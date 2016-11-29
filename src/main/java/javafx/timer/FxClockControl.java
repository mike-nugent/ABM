package javafx.timer;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import utils.Times;

public class FxClockControl extends Pane
{
    final Label _minSecTxt = new Label("10:00");
    final Label _msTxt     = new Label("00");

    Button         _startStopBtn  = new Button();
    Button         _resetBtn      = new Button();
    protected long _startTime     = 0;
    long           _countDownFrom = Times.TEN_MIUTES;

    AnimationTimer animationtimer;

    public FxClockControl()
    {
        // this.setMouseTransparent(true);
        // Set the clock text

        _resetBtn.setDisable(true);
        _startStopBtn.setDisable(false);
        _minSecTxt.setStyle("-fx-font-size: 85px;" + "-fx-font-weight: bold;");
        _msTxt.setStyle("-fx-font-size: 30px;" + "-fx-font-weight: bold;");

        _msTxt.setLayoutX(220);
        _msTxt.setLayoutY(20);

        // Set the buttons
        _startStopBtn.setText("Start");
        _startStopBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                if (_startStopBtn.getText().equals("Start"))
                {
                    startClock();
                }
                else if (_startStopBtn.getText().equals("Stop"))
                {
                    stopClock();

                }
            }

        });

        _resetBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                resetClock();
            }

        });

        _startStopBtn.setMinWidth(50);
        _resetBtn.setText("Reset");
        _resetBtn.setMinWidth(50);

        final VBox btnGrp = new VBox();
        btnGrp.getChildren().addAll(_startStopBtn, _resetBtn);

        btnGrp.setLayoutX(270);
        btnGrp.setLayoutY(40);

        this.getChildren().addAll(_minSecTxt, _msTxt, btnGrp);
    }

    private void resetClock()
    {
        if (animationtimer != null)
        {
            animationtimer.stop();
            animationtimer = null;
            _minSecTxt.setText("10:00");
            _msTxt.setText("00");

            _minSecTxt.setTextFill(new Color(0, 0, 0, 1));
            _msTxt.setTextFill(new Color(0, 0, 0, 1));
        }

        _startStopBtn.setText("Start");
        _startStopBtn.setDisable(false);
        _resetBtn.setDisable(true);
    }

    private void stopClock()
    {
        if (animationtimer != null)
        {
            animationtimer.stop();
        }
        _startStopBtn.setText("Start");
        _startStopBtn.setDisable(true);
    }

    private void startClock()
    {
        if (animationtimer == null)
        {
            _startTime = System.currentTimeMillis();

            animationtimer = new AnimationTimer()
            {
                @Override
                public void handle(final long now)
                {
                    // Countup
                    final long diff = System.currentTimeMillis() - _startTime;
                    long countdown = _countDownFrom - diff;

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

                    final Color c = getColorBasedOnTime(countdown);
                    final String time = Times.getMinSec(countdown);

                    _minSecTxt.setText(time);
                    _msTxt.setText(milliSec);
                    _minSecTxt.setTextFill(c);
                    _msTxt.setTextFill(c);

                    if (countdown == 0)
                    {
                        stopClock();
                    }
                }
            };
        }

        animationtimer.start();
        _startStopBtn.setText("Stop");
        _resetBtn.setDisable(false);
    }

    protected Color getColorBasedOnTime(final long diff)
    {

        final double scale = ((double) diff / (double) _countDownFrom);
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

package javafx.timer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import sounds.SoundManager;

public class FxClockController extends Pane
{
    Button _startStopBtn = new Button();
    Button _resetBtn     = new Button();

    public FxClockController(final FxClock clock)
    {
        _resetBtn.setDisable(true);
        _startStopBtn.setDisable(false);
        _startStopBtn.setMinWidth(50);
        _resetBtn.setMinWidth(50);
        _resetBtn.setText("Reset");
        _startStopBtn.setText("Start");

        _startStopBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                SoundManager.playTickSound();

                if (_startStopBtn.getText().equals("Start"))
                {
                    clock.startClock();
                    _startStopBtn.setText("Stop");
                    _resetBtn.setDisable(false);
                }
                else if (_startStopBtn.getText().equals("Stop"))
                {
                    clock.stopClock();
                    _startStopBtn.setText("Start");
                    _startStopBtn.setDisable(true);

                }
            }

        });

        _resetBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                SoundManager.playTickSound();

                clock.resetClock();
                _startStopBtn.setText("Start");
                _startStopBtn.setDisable(false);
                _resetBtn.setDisable(true);
            }

        });

        final VBox btnGrp = new VBox();
        btnGrp.getChildren().addAll(_startStopBtn, _resetBtn);
        this.getChildren().add(btnGrp);
    }
}

package javafx.timer;

import gameinfo.IconLoader;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import sounds.SoundManager;

public class FxClockController extends Pane
{
    Image startIcon = IconLoader.loadFxImage("play-icon.png", 25);
    Image stopIcon  = IconLoader.loadFxImage("stop-icon.png", 25);
    Image resetIcon = IconLoader.loadFxImage("reset-icon.png", 25);

    ImageView _startStopBtn = new ImageView(startIcon);
    FxClock   _clock;

    public FxClockController(final FxClock clock)
    {
        _clock = clock;
        _startStopBtn.setVisible(true);
        _startStopBtn.setOnMouseClicked(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(final MouseEvent e)
            {
                SoundManager.playTickSound();

                if (!_clock.isPlaying() && _clock.isReset())
                {
                    clock.startClock();
                    _startStopBtn.setImage(stopIcon);
                }
                else if (_clock.isPlaying() && !_clock.isReset())
                {
                    clock.stopClock();
                    _startStopBtn.setImage(resetIcon);
                }
                else if (!_clock.isPlaying() && !_clock.isReset())
                {
                    clock.resetClock();
                    _startStopBtn.setImage(startIcon);
                }
                
            }

        });


        final StackPane btnGrp = new StackPane();
        btnGrp.setPadding(new Insets(10));
        btnGrp.getChildren().addAll(_startStopBtn);
        this.getChildren().add(btnGrp);
    }
}

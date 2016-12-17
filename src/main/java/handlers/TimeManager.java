package handlers;

import javafx.timer.FxClock;
import main.DisplayManager;

public class TimeManager
{

    public static void startTime(final String timeData)
    {
        final String mode = timeData.substring(0, timeData.indexOf(":"));
        final String time = timeData.substring(timeData.indexOf(":") + 1);

        final String[] split = time.split(":");
        final int minutes = Integer.parseInt(split[0]);
        final int seconds = Integer.parseInt(split[1]);

        System.out.println(minutes + " " + seconds);

        final long millisec = (minutes * 60 * 1000) + (seconds * 1000);

        if (!DisplayManager.isClockShowing())
        {
            DisplayManager.showClockPopup();
        }

        FxClock.startClock(mode, millisec);
    }
}

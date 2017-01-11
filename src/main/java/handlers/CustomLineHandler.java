package handlers;

import java.util.regex.Pattern;

import fx.screens.AlertScreen;
import gameinfo.ScriptData;
import javafx.application.Platform;
import sounds.SoundManager;

public class CustomLineHandler extends LineHandler
{
    private ScriptData data;

    public CustomLineHandler(final ScriptData data)
    {
        super(Pattern.compile(("*" + data.getLine() + "*").replaceAll("\\*", X)));

        this.data = data;

        /*
         * String time = data.getTime(); if(time != null) {
         * TimeManager.startTime(time); }
         */
    }

    public void updateLineHandler(final ScriptData newData)
    {
        this.data = newData;

        System.out.println("Updating line handler.");
        System.out.println("incoming: " + newData.getLine());
        System.out.println("old: [" + _patterns[0] + "]");
        _patterns = new Pattern[]
        { Pattern.compile(("*" + data.getLine() + "*").replaceAll("\\*", X)) };

        System.out.println("new: [" + _patterns[0] + "]");
    }

    @Override
    protected void handleLine(final String line, final boolean isCurrent)
    {

        // Custom parsers ignore the recent history, and only go off the new
        // stuff
        if (!isCurrent)
        {
            return;
        }

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                // Do Time stuff
                System.out.println("Custom line handler captured!  Found line: " + line);
                final String time = data.getTime();
                if (time != null)
                {
                    TimeManager.startTime(time);
                }

                // Do Sound stuff
                final String sound = data.getSound();
                if (sound != null)
                {
                    SoundManager.playSound(sound);
                }
                
                // Do Alert stuff
                final String alert = data.getAlert();
                if(alert != null)
                {
                	AlertScreen.showAlert(alert);
                }
            }
        });

    }

    public int getID()
    {
        return data.getID();
    }
}

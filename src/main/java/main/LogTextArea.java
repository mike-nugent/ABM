package main;

import javafx.scene.control.TextArea;

public class LogTextArea extends TextArea
{
    private boolean pausedScroll   = false;
    private double  scrollPosition = 0;

    public LogTextArea()
    {
        super();
    }

    public void setMessage(final String data)
    {
        if (pausedScroll)
        {
            scrollPosition = this.getScrollTop();
            this.setText(data);
            this.setScrollTop(scrollPosition);
        }
        else
        {
            this.setText(data);
            this.setScrollTop(Double.MAX_VALUE);
            this.appendText("");
        }
    }

    public void pauseScroll(final Boolean pause)
    {
        pausedScroll = pause;
    }

}
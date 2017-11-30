package main;

import javafx.stage.Stage;

public class ABMStage
{
    private static Stage   _stage;
    private static boolean _windowLock = false;

    public static Stage getStage()
    {
        return _stage;
    }

    public static void setStage(final Stage stage)
    {
        _stage = stage;
    }

    public static void setWindowLock(final Boolean new_val)
    {
        _windowLock = new_val;
    }

    public static boolean getWindowLock()
    {
        return _windowLock;
    }

}

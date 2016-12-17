package handlers;

import java.util.ArrayList;
import java.util.List;

import fx.screens.ScriptsUpdater;
import gameinfo.ScriptData;

public class HandlerManager
{
    private static List<CustomLineHandler> handlers;

    public static List<CustomLineHandler> getCustomHandlers()
    {
        if (handlers == null)
        {
            handlers = new ArrayList<CustomLineHandler>();
            final List<ScriptData> scripts = ScriptsUpdater.getAllScripts();
            for (final ScriptData data : scripts)
            {
                final CustomLineHandler handler = new CustomLineHandler(data);
                handlers.add(handler);
            }
        }
        return handlers;
    }

    public static void addNewHandler(final ScriptData data)
    {
        final CustomLineHandler handler = new CustomLineHandler(data);
        handlers.add(handler);
        LineHandler.addNewCustomHandler(handler);
    }

    /**
     * Update the handler with the matching ID
     *
     */
    public static void updateLineHandler(final ScriptData data)
    {
        for (final CustomLineHandler handler : handlers)
        {
            if (handler.getID() == data.getID())
            {
                System.out.println("Found match ID = " + data.getID());
                System.out.println("Just before: " + data.getCompactedScript());
                handler.updateLineHandler(data);
                return;
            }
        }
    }
}

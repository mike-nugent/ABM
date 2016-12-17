package logreader;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import handlers.LineHandler;

public class AionLogResponder extends AionTailerListenerAdapter
{
    public static int MAX_LINES_TO_SAVE = 1000;

    List<LineHandler>   handlers;
    static List<String> recentLines = Collections.synchronizedList(new LinkedList<String>());

    public AionLogResponder()
    {
        handlers = LineHandler.getOrCreateHandlers();
    }

    @Override
    public void handle(final String line)
    {
        try
        {
            addNewLine(line);
            for (final LineHandler handler : handlers)
            {
                if (handler.handlesLine(line))
                {
                    handler.handle(line, true);
                    return;
                }
            }

            // System.out.println("not handled by LineHandlers: " + line);
        }
        catch (final Exception e)
        {
            System.out.println("Error in " + this + " caught exception was unexpected");
            e.printStackTrace();
        }
    }

    private static void addNewLine(final String line)
    {
        if (recentLines.size() >= MAX_LINES_TO_SAVE)
        {
            recentLines.remove(0);
        }
        recentLines.add(line);
    }

    public static List<String> getRecentLines()
    {
        return recentLines;
    }
}
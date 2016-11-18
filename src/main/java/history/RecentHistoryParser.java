package history;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import config.ConfigFile;
import handlers.LineHandler;
import tasks.Task;
import xforms.TransformBar;

public class RecentHistoryParser
{
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    List<LineHandler>       handlers;

    List<String>       recentLogActivity = new LinkedList<String>();
    private final Date _currentDate;

    public RecentHistoryParser()
    {
        handlers = LineHandler.getOrCreateHandlers();
        _currentDate = new Date();
    }

    public void setup()
    {

    }

    public boolean parseAndKeepReading(final String line, final Task task)
    {
        final Date logDate = LineHandler.getDateTime(line);

        if (logDate != null)
        {
            final long time = _currentDate.getTime() - logDate.getTime();

            task.setInfoString(line);

            // If testing locally, then ignore the timestamps and process all
            // lines in the log file.
            if (ConfigFile.getLogFile().getName().equals(ConfigFile.DEBUG_LOG_FILE_NAME)
                    || time <= TransformBar.TWO_HOURS)
            {

                for (final LineHandler handler : handlers)
                {
                    if (handler.handlesLine(line))
                    {
                        System.out.println("Found a line to save:" + line);
                        recentLogActivity.add(line);
                        return true;
                    }
                }
                return true;
            }
            else
            {

                return false;
            }
        }
        else
        {
            // keep reading, shitty line but w/e
            return true;
        }
    }

    public void teardown()
    {
        // Since this was a backwards-parse of the log, we need to reverse the
        // entries to look at
        Collections.reverse(recentLogActivity);

        // Handle each line saved
        for (final String s : recentLogActivity)
        {
            for (final LineHandler handler : handlers)
            {
                if (handler.handlesLine(s))
                {
                    handler.handle(s);
                    break;
                }
            }
        }
    }
}

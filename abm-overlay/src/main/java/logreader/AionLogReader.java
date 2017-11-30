package logreader;

import java.io.File;

import config.ConfigFile;

public class AionLogReader
{
    private static boolean _isRunning = false;

    public static boolean readLog()
    {

        final File configFile = ConfigFile.getLogFile();
        if (!configFile.exists())
        {
            // TODO - periodically pole here on the file. Change to an asynchronous thread daemon running in the background.
            System.out.println(AionLogReader.class + " Error, could not read the log file: " + configFile + " does it exist?");
            return false;
        }
        _isRunning = true;

        final Runnable r = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    System.out.println("Starting to read log...");
                    openFileAndStartReading();
                }
                catch (final Exception x)
                {
                    // in case ANY exception slips through
                    x.printStackTrace();
                }
            }
        };

        final Thread internalThread = new Thread(r);
        internalThread.start();
        return true;
    }

    protected static void openFileAndStartReading()
    {
        AionTailer tailer = null;
        try
        {
            System.out.println("Reading in erf");
            final AionTailerListener listener = new AionLogResponder();

            final File file = ConfigFile.getLogFile();

            if (!file.exists())
            {
                System.out.println("Error, the log file: " + file + " does not exist. exiting;");
                return;
            }

            tailer = new AionTailer(file, listener, 50, true);
            System.out.println("starting the Trailer...");
            tailer.run();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (tailer != null)
            {
                tailer.stop();
            }

            _isRunning = false;
        }
    }

    public static boolean isRunning()
    {
        return _isRunning;
    }
}

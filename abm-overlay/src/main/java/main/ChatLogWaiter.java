package main;

import java.io.File;

import tasks.Task;
import tasks.TaskState;

public class ChatLogWaiter implements Runnable
{
    File         logFile;
    private Task returnTask;

    public ChatLogWaiter(final File logFile)
    {
        this.logFile = logFile;
    }

    public Task waitForChatFile()
    {
        if (returnTask == null || returnTask.isTerminal())
        {
            returnTask = new Task();
            final Thread internalThread = new Thread(this);
            internalThread.start();
        }
        return returnTask;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                // Sleep and poll every second until the Chat.log file is found.
                Thread.sleep(1000);
                System.out.println("Checking to see if the Chat.log file has been created...");

                if (logFile.exists())
                {
                    System.out.println(logFile.getAbsolutePath() + " has been created!");
                    returnTask.setTaskState(TaskState.Completed);
                    return;
                }
                else
                {
                    System.out.println("file: " + logFile.getAbsolutePath() + " doesn't exist yet.");
                }
            }
            catch (final Exception x)
            {
                x.printStackTrace();
                System.out.println("Could not check the Chat.log file: " + x);
            }
        }
    }

}

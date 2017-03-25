package main;

import java.io.File;

public class ChatLogWaiter implements Runnable
{
	File logFile;
	public ChatLogWaiter(File logFile) 
	{
		this.logFile = logFile;
	}
	
	public void waitForChatFile()
	{
		Thread internalThread = new Thread(this);
		internalThread.start();
	}

	@Override
	public void run() 
	{
		while(true)
		{
			try 
			{
				//Sleep and poll every second until the Chat.log file is found.
				Thread.sleep(1000);
				System.out.println("Checking to see if the Chat.log file has been created...");
				
				if(logFile.exists())
				{
					System.out.println(logFile.getAbsolutePath() + " has been created!");
					
					return;
				}
				else
				{
					System.out.println("file: " + logFile.getAbsolutePath() + " doesn't exist yet.");
				}
			}
			catch (Exception x) 
			{
				x.printStackTrace();
				System.out.println("Could not check the Chat.log file: " + x);
			}
		}
	}

}


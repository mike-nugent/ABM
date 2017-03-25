package logreader;

import java.io.File;

import config.ConfigFile;
import main.MainSwing;

public class AionLogReader 
{
	private static boolean _isRunning = false;
 
	public static boolean readLog() 
	{
		
		File configFile = ConfigFile.getLogFile();
		if(!configFile.exists())
		{
			//TODO - periodically pole here on the file. Change to an asynchronous thread daemon running in the background.
			System.out.println(AionLogReader.class + " Error, could not read the log file: " + configFile + " does it exist?");
			return false;
		}
		_isRunning = true;
		
		Runnable r = new Runnable() 
		{
			public void run() 
			{
				try 
				{
					System.out.println("Starting to read log...");
					openFileAndStartReading();
				}
				catch (Exception x) 
				{
					// in case ANY exception slips through
					x.printStackTrace();
				}
			}
		};

		Thread internalThread = new Thread(r);
		internalThread.start();
		return true; 
	}

	protected static void openFileAndStartReading() 
	{ 
		AionTailer tailer = null;
		try {
			System.out.println("Reading in erf");
			AionTailerListener listener = new AionLogResponder();
			
			File file = ConfigFile.getLogFile();
	        
	        if( !file.exists())
	        {
	        	System.out.println("Error, the log file: " + file + " does not exist. exiting;" );
	        	return;
	        }
			
			tailer = new AionTailer(file, listener, 500, true);
			System.out.println("starting the Trailer...");
			tailer.run();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			if (tailer != null)
				tailer.stop();
			
			_isRunning = false;
		}
	}

	public static boolean isRunning() 
	{
		return _isRunning ;
	}
}

	 

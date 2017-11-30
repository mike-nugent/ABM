package history;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.input.ReversedLinesFileReader;

import config.ConfigFile;
import tasks.Task;
import tasks.TaskState;

/**
 * A simple log scanning class, that takes in a path of a file to read.
 * The scanFile(List<Parsers> parsers) method will scan the file, and 
 * will deliver the line to each of the parsers added.
 *
 */
public class QuickHistoryLineScanner 
{ 
	public Task scanFile(final RecentHistoryParser parser) 
	{ 
		final Task returnTask = new Task();
		Runnable r = new Runnable() 
		{
			public void run() 
			{
				try 
				{
					System.out.println("Starting to read log...");
					openFileAndStartReading(returnTask, parser);
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
		return returnTask;
	}
	
	 
	private void openFileAndStartReading(Task task, RecentHistoryParser parser)
	{
		File inFile = ConfigFile.getLogFile();
		if(!inFile.exists())
		{
			String errorMessage = QuickHistoryLineScanner.class + " Error, could not read the log file: " + inFile + " does it exist?";
			System.out.println(errorMessage);
			task.setErrorMessage(errorMessage);
			task.setTaskState(TaskState.Error);
		}
 
		parser.setup();
		System.out.println("reading the file: " + inFile);
		ReversedLinesFileReader reader = null;
		try
		{
			reader = new ReversedLinesFileReader(inFile);
			String line = null;
			while ((line = reader.readLine()) != null) 
			{
				boolean keepReading = parser.parseAndKeepReading(line, task);
				if(!keepReading)
				{
					reader.close();
					reader = null;
					task.setTaskState(TaskState.Completed);
					return;
				}
			}  	
		} 
		catch (Exception e)
		{
			System.out.println("Caught error");
			e.printStackTrace();
		}
		finally
		{ 
			parser.teardown();
			if(reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			task.setTaskState(TaskState.Completed);
		}
	}
}

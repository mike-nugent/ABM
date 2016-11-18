package utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CannedData 
{
	public static List<String> activeTop100List() 
	{
		List<String> ret = new ArrayList<String>(); 

		try 
		{

			File inFile = new File("src/main/resources/top-100-player-list.txt");
			FileInputStream inputStream = null;
			Scanner sc = null;

			try 
			{
				inputStream = new FileInputStream(inFile);
				sc = new Scanner(inputStream, "UTF-8");
				while (sc.hasNextLine()) 
				{
					String line = sc.nextLine();
					ret.add(line);
				}
				// note that Scanner suppresses exceptions
				if (sc.ioException() != null) 
				{
					throw sc.ioException();
				}
			} 
			finally 
			{
				if (inputStream != null) 
				{
					inputStream.close();
				}
				if (sc != null)
				{
					sc.close();
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace(); 
		}
		
		return ret;
	}
}

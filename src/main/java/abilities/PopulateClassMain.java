package abilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import config.ConfigFile;
import gameinfo.AbilityData;
import gameinfo.PlayerClassFinder;
import gameinfo.PlayerData;
import gameinfo.Server;
import handlers.InflictDamageLineHandler;
import handlers.TransformLineHandler;
import handlers.UsesCooldownLineHandler;

public class PopulateClassMain 
{
	
	public static void main(String[] args)
	{
		File inFile = ConfigFile.getLogFile();
		FileInputStream inputStream = null;
		Scanner sc = null;
		InflictDamageLineHandler dh = new InflictDamageLineHandler();
		UsesCooldownLineHandler ch = new UsesCooldownLineHandler();
		TransformLineHandler th = new TransformLineHandler();
		
		try 
		{
			inputStream = new FileInputStream(inFile);
			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine()) 
			{
				String line = sc.nextLine();
				if(dh.handlesLine(line))
				{
					AbilityData data = dh.parseAbilityData(line);
					if(data == null)
					{
						//ERROR CASES System.out.println("no data for: " + line);
					}
					else if(data.casterServer.equals(Server.Unknown))
					{
						System.out.println("Ability line found: " + line);
					}
					PlayerClassFinder.updatePlayerByAbility(data);

				}
				else if (ch.handlesLine(line))
				{
					AbilityData data = ch.parseCooldownLine(line);
					PlayerClassFinder.updatePlayerByAbility(data);
				}
				else if(th.handlesLine(line))
				{
					PlayerData data = th.parseTransformLine(line);
					PlayerClassFinder.updatePlayer(data);
				}				
			}
			
			//Now write the list to files:
			PlayerClassFinder.flushDataToFiles();
			
		} 
		catch (FileNotFoundException e) 
		{			
			e.printStackTrace();
		}  
		finally 
		{	
			if (inputStream != null) 
			{
				try 
				{
					inputStream.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			if (sc != null) 
			{
				sc.close();
			}
		}		
	}
}

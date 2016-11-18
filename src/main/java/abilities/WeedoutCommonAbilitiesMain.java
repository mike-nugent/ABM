package abilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import gameinfo.Archetype;

public class WeedoutCommonAbilitiesMain 
{
	public static void main(String[] args) 
	{
		Map<String, List<Archetype>> abilityMap = new HashMap<String, List<Archetype>>();

		for (Archetype a : Archetype.values()) 
		{
			if (!a.equals(Archetype.Unknown)) 
			{
				String playerFile = "src/main/resources/abilities/"+a.name() + ".abilities";
				File inFile = new File(playerFile);
				FileInputStream inputStream = null;
				Scanner sc = null;

				try 
				{
					inputStream = new FileInputStream(inFile);
					sc = new Scanner(inputStream, "UTF-8");
					while (sc.hasNextLine()) 
					{
						String line = sc.nextLine();
						String abilityName = line.trim();
						List<Archetype> classList = abilityMap.get(abilityName);
						if(classList == null)
						{
							classList = new ArrayList<Archetype>();
							classList.add(a);
						}
						else
						{
							if(!classList.contains(a))
							{
								classList.add(a);
							}
						}
						
	 
						
						abilityMap.put(abilityName, classList);
					}
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
		
		
		
		String ignoreList = "";
		for(String key : abilityMap.keySet())
		{
			List<Archetype> abs = abilityMap.get(key);

			if(abs.size() > 1)
			{
				ignoreList += key + "\n";
				System.out.println(key);
				for(Archetype arc : abs)
				{
					System.out.println("	" + arc); 
				}
			}
		} 
		
		try 
		{
			FileUtils.writeStringToFile(new File("src/main/resources/abilities/Common.abilities"), ignoreList, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

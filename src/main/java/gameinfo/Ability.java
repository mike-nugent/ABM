package gameinfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Ability 
{
	static Map<String, Archetype > abilityMap = loadMapping();
	public static Archetype locateArchetype(String ability) 
	{
		if(abilityMap.containsKey(ability))
		{
			return abilityMap.get(ability);
		}
		else
		{
			return Archetype.Unknown;
		}
	}
	
	private static Map<String, Archetype> loadMapping() 
	{
		Map<String, Archetype> returnMap = new HashMap<String, Archetype>();
		
		List<String> commonAbilities = loadAbilities("Common");
		for(Archetype a : Archetype.values())
		{
			if(!a.equals(Archetype.Unknown))
			{
				List<String> aAbilities = loadAbilities(a.name());
				for(String ability : aAbilities)
				{
					if(!commonAbilities.contains(ability))
					{
						returnMap.put(ability.trim(), a);
					}
				}
			}
		}
		
		return returnMap;
	}

	private static List<String> loadAbilities(String name) 
	{
		List<String> returnList = new ArrayList<String>();
		String abilityFile  = "src/main/resources/abilities/"+name + ".abilities";
		File inFile = new File(abilityFile);
		FileInputStream inputStream = null;
		Scanner sc = null;
		
		try 
		{
			inputStream = new FileInputStream(inFile);
			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine()) 
			{
				String line = sc.nextLine();
				if(line != null && line.trim().length() > 0)
				{
					returnList.add(line);
				}				
			}
		} catch (FileNotFoundException e) 
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
		
		return returnList;
	}


}

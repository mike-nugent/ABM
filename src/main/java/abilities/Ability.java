package abilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import gameinfo.Archetype;
import xforms.XformLoader;

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
		String abilityFile  = name + ".abilities";
		Scanner sc = null;
        InputStream in = null;

		
		try 
		{
            in = Ability.class.getResourceAsStream(abilityFile);
            sc = new Scanner(in, "UTF-8");
            while (sc.hasNextLine())
            {
                final String line = sc.nextLine();
                if(line != null && line.trim().length() > 0)
				{
					returnList.add(line);
				}	
            }	
		} 
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (final IOException e)
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

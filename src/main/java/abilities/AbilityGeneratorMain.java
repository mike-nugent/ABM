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

import config.ConfigFile;
import gameinfo.AbilityData;
import handlers.InflictDamageLineHandler;
import handlers.UsesCooldownLineHandler;

public class AbilityGeneratorMain 
{	
	
	/** 
	 * Note that this list below is just heuristic data used to find the abilities in the game.
	 * It has nothing to do with what will be in the final release of the tool.
	 *
	 */
	private enum PlayerClassMapping
	{
		Aethertech ("ThunderSpank", "HeIlFire"),
		Assassin ("Rammsteinx", "Niightwing", "Carve"),
		Cleric ("Zhule", "Alphea", "Nyinu", "Audrin", "Allessandra"),
		Chanter ("NiaLynn", "Pixelz", "Seventeenth", "Tincho"),
		Gladiator("Orelia", "xChops", "Moist", "iGouki", "Garmrz"),
		Gunslinger ("NoelVermillion","Criminal", "DouDou", "DrLiu"),
		Sorcerer ("Legaccy", "Evolett", "Licjtgestalt", "Evil", "xHermionex"),
		Spiritmaster ("MeIIi", "TakyzO", "DrDot", "ThunderDot", "LunyWitch"),
		Songweaver ("MolotovCocktease", "Serah","elmma","FoxxyLady","oONatsumiOo"),
		Ranger ("Aioroz", "Zakku", "Boonie","Seumacho","Drakarys"),
		Templar ("Ahdol", "Sasorri", "rajput", "Salyvan", "Yardos");
		
		private String[] _names;
		private PlayerClassMapping(String ...names)
		{
			_names = names;
		}
		
		public String[] getNames()
		{
			return _names;
		}
	}
	

	public static void main(String[] args)
	{
		File inFile = ConfigFile.getLogFile();
		FileInputStream inputStream = null; 
		Scanner sc = null;
		Map<PlayerClassMapping, List<String>> abilitiesMap = new HashMap<PlayerClassMapping, List<String>>();
		
		InflictDamageLineHandler dh = new InflictDamageLineHandler();
		UsesCooldownLineHandler ch = new UsesCooldownLineHandler();
		
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
					updateData(data, line, abilitiesMap);
				}
				else if (ch.handlesLine(line))
				{
					AbilityData data = ch.parseCooldownLine(line);
					updateData(data,line, abilitiesMap); 
				}
			}
		
			 
			for(PlayerClassMapping mp : abilitiesMap.keySet())
			{			
				String data = "";
				List<String> list = abilitiesMap.get(mp);
				for(String s : list)
				{
					data += s.trim() + "\n";
					System.out.println(s);	
				}
				
				
				try {
					FileUtils.writeStringToFile(new File("src/main/resources/abilities/"+mp.name()+".abilities"), data, false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
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

	private static void updateData(AbilityData data, String line, Map<PlayerClassMapping, List<String>> abilitiesMap) 
	{
		if(data == null)
		{
			System.out.println("could not parse line: " + line);
			return;
		}
		PlayerClassMapping clazz = isFromPlayerList(data.caster);
		if(clazz != null)
		{		
			//Then we can use this data.  Store the ability with the class that uses it.
			List<String> abilities = abilitiesMap.get(clazz);
			if(abilities == null)
			{
				abilities = new ArrayList<String>();
				abilities.add(data.ability);
				System.out.println("Adding: " + data.ability);
			}
			else
			{
				if(!abilities.contains(data.ability)) 
				{
					System.out.println("Adding: " + data.ability);
					abilities.add(data.ability);
				}
			}
			abilitiesMap.put(clazz, abilities);
		}		
	}

	private static PlayerClassMapping isFromPlayerList(String name) 
	{ 
		for(PlayerClassMapping mp : PlayerClassMapping.values())
		{
			for(String n : mp.getNames() )
			{
				if(name.equals(n))
				{
					return mp;
				}
			}
		}

		return null;
	}
}

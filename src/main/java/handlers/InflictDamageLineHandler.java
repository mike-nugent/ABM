package handlers;

import java.util.Date;

import config.ConfigFile;
import gameinfo.AbilityData;
import gameinfo.Server;
import main.Main;

public class InflictDamageLineHandler extends LineHandler 
{

	/**
	 * 	(X + "inflicted" + X + "damage on" + X)
		(X + "inflicted" + X + "damage on" + X + "by using" + X)
		
		2015.12.03 21:05:34 : Shinde inflicted 4,015 damage on Ulsaruk by using Cinder Cannon.
		2015.12.03 21:05:34 : LunaWhiskerz inflicted 1,502 damage on Ulsaruk.
		2015.12.03 21:05:34 : LunaWhiskerz inflicted 1,147 damage on Ulsaruk.
		2015.12.03 21:05:34 : LunaWhiskerz inflicted 114 damage on Ulsaruk.
		2015.12.03 21:05:34 : FayeVaIentine inflicted 1,367 damage on Kysis Ereshkigal Bonebreaker by using Gale Arrow.
		2015.12.03 21:05:34 : Waiam inflicted 1,424 damage on Kysis Ereshkigal Bonebreaker.
		2015.12.03 21:05:34 : Megumu inflicted 6,276 damage on Ulsaruk by using Sonic Gust.
		2015.12.03 21:05:34 : Kherigan inflicted 2,524 damage on Kysis Ereshkigal Bonebreaker by using Crosstrigger.
		2015.12.03 21:05:34 : Judasiscariote inflicted 5,336 damage on Kysis Ereshkigal Bonebreaker by using Weaken Spirit.
		2015.12.03 21:05:34 : Critical Hit!Anyelka inflicted 3,868 damage on Ulsaruk by using Pressure Wave.
		2015.12.03 21:05:34 : MikeTwaiks inflicted 958 damage on Kysis Ereshkigal Bonebreaker by using Deadshot.

	 */
	public InflictDamageLineHandler()
	{
		super( auto_attack, ability_attack);
	}

	public AbilityData parseAbilityData(String line)
	{
		try
		{
			if(line.contains("reflected"))
			{
				//ignore
				return null;
			}
			
			String trimmed = line.substring(line.indexOf(" : ") + 3);
			trimmed  = trimmed.replace("Critical Hit!", "");
			String caster =  trimmed.substring(0, trimmed.indexOf("inflicted "));
			if(caster.contains(" has"))
			{
				caster = caster.replace(" has ", "");
			}
			
			String casterServer = caster.contains("-") ? caster.substring(caster.indexOf("-") + 1) : ConfigFile.getServer().getServerString();
			
			//target needs TBD based on auto attack or not.
			String target = null; 
			
			//ability may not exist if auto attack.
			String ability = null;
			
			if(trimmed.contains(" by using "))
			{
				ability = trimmed.substring(trimmed.lastIndexOf(" by using ") + 10, trimmed.lastIndexOf("."));
				target = trimmed.substring(trimmed.indexOf(" damage on ") + 11, trimmed.indexOf(" by using "));
			}
			else
			{
				ability = "Auto Attack";
				target = trimmed.substring(trimmed.indexOf(" damage on ") + 11, trimmed.lastIndexOf("."));
			}
			String targetServer = target.contains("-") ? target.substring(target.indexOf("-") + 1) : ConfigFile.getServer().getServerString();

			
			if(target.contains("-"))
				target = target.substring(0, target.indexOf("-"));
			
			if(caster.contains("-"))
				caster = caster.substring(0, caster.indexOf("-"));
			
			
			Date D = getDateTime(line);
			String T = target.trim();
			String C = caster.trim();
			String A = ability.trim();
			Server TS = Server.getServer(targetServer.trim());
			Server CS = Server.getServer(casterServer.trim());

			AbilityData data =  new AbilityData(D, T, C, A, TS, CS);
			return data;
		}
		catch (Exception e)
		{
			showError(e, line);
			return null;
		}
	}
	
	@Override
	protected void handleLine(String line) 
	{
		AbilityData data = parseAbilityData(line);
		//_main.abilityDetected(data);
	}
}

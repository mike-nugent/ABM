package gameinfo;

import java.util.Date;

public class AbilityData 
{
	public Date date;
	public String target, caster, ability;
	public Server casterServer, targetServer;
	public AbilityData(Date date, String target, String caster, String ability, Server targetServer, Server casterServer) 
	{
		this.date = date;
		this.target = target;
		this.caster = caster;
		this.ability = ability;
		this.casterServer = casterServer;
		this.targetServer = targetServer;
	}

	/*public static AbilityData parseDamage(String line) 
	{
		String trimmed = line.substring(line.indexOf(" : ") + 3);
		trimmed  = trimmed.replace("Critical Hit!", "");
		String name =  trimmed.substring(0, trimmed.indexOf(" "));
		String server = name.contains("-") ? name.substring(name.indexOf("-") + 1) : ConfigFile.getServer().getServerString();
		String ability = trimmed.substring(trimmed.lastIndexOf(" by using ") + 10, trimmed.lastIndexOf("."));
		
		if(name.contains("-"))
		{
			name = name.substring(0, name.indexOf("-"));
		}
		String N = name.trim();
		Server S = Server.getServer(server);
		String A = ability.trim();
		return new AbilityData(N, S, A);
		
	}*/
}

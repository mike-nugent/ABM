package gameinfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import config.ConfigFile;
import main.RandomNameGenerator;

public class PlayerData 
{
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	
	public String name;
	public Server server;
	public Rank rank;
	public Race race;
	public Archetype clazz;
	public Date dateTime;
	public String location;
	
	public PlayerData(
			Date dateTime, 
			String name, 
			Server server, 
			Race race, 
			Rank rank, 
			Archetype clazz,
			String location) 
	{ 
		this.dateTime = dateTime;
		this.name = name;
		this.server = server;
		this.rank = rank;
		this.race = race;
		this.clazz = clazz;
		this.location = location;
	}

	  

	public static PlayerData parseXform(String xformLine) 
	{
		String[] parsed = xformLine.split(" "); 
		String date = parsed[0];
		String time = parsed [1];
		String name = parsed[3];
		String server = name.contains("-") ? name.substring(name.indexOf("-") + 1) : ConfigFile.getServer().getServerString();
		String race = xformLine.contains("Asmodian") ? "Asmodian" : "Elyos";
		String rank = xformLine.substring(xformLine.indexOf("Guardian General ") + 16, xformLine.lastIndexOf(" in "));
		String location = xformLine.substring(xformLine.lastIndexOf(" in ") + 4, xformLine.lastIndexOf(".")); 
				
		if(name.trim().equals(ConfigFile.getName()))
		{
			name = ConfigFile.getName();
		}
		
		if(name.contains("-"))
		{
			name = name.substring(0, name.indexOf("-"));
		}
		
		date = date.trim();
		time = time.trim();
		name = name.trim();
		server = server.trim();
		race = race.trim();
		rank = rank.trim();
		location = location.trim();
		
		Date DT = null;
		try
		{
			DT = formatter.parse(date + " " + time);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		String N = name;
		Server S = Server.getServer(server);
		Race RA = Race.getRace(race);
		Rank R = Rank.getRank(rank);
		
		//Has to be looked up by name
		Archetype C = Archetype.getArchetype(name, S, RA); 
		String L = location;
		
		
		PlayerData data = new PlayerData(DT, N, S, RA, R, C, L);
		
		return data;
	}

	private static String fixDeathLineForParsing(String line) 
	{
		if(line.contains("Officer"))
		{
			return line.replace("Officer", "START:");
		}
		else if (line.contains("General"))
		{
			 return line.replace("General", "START:");
		}
		else if (line.contains("Commander"))
		{
			return line.replace("Commander", "START:");
		}
		else if (line.contains("Governor"))
		{
			return line.replace("Governor", "START:");
		}
		
		System.out.println("ERROR - could not transform line: " +  line);
		return line;
	}
	
	public static Date getDateTime(String line)
	{
		String[] parsed = line.split(" "); 
		String date = parsed[0];
		String time = parsed [1];
		String dateTime = (date.trim() + " " + time.trim()).trim();
		try 
		{
			return formatter.parse(dateTime);
		}
		catch (ParseException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
  
	public static PlayerData parseDeath(String line) 
	{
		String reWorkedLine = fixDeathLineForParsing(line);
		String name = reWorkedLine.substring(reWorkedLine.indexOf("START:") + 6, reWorkedLine.indexOf(" has died in "));
		String server = name.contains("-") ? name.substring(name.indexOf("-") + 1) : ConfigFile.getServer().getServerString();
		String race = line.contains("Asmodian") ? "Asmodian" : "Elyos";
		String location = line.substring(line.lastIndexOf(" in ") + 4, line.lastIndexOf(".")); 


		if(name.contains("-"))
		{
			name = name.substring(0, name.indexOf("-")).trim();
		}
		
		if(name.trim().equals(ConfigFile.getName()))
		{
			name = ConfigFile.getName();
		}
		
		String rank = line.substring(line.lastIndexOf(" Army ") + 6, line.indexOf(name)).trim();
		
		Date DT = getDateTime(line);
		String N = name.trim();
		Server S = Server.getServer(server);
		Race R = Race.getRace(race);
		Rank RA = Rank.getRank(rank);
		Archetype C = Archetype.getArchetype(name, S, R);
		String L = location.trim();
		
		
		return new PlayerData(DT, N, S, R, RA, C, L); 
	}



	public static PlayerData generateRandom() 
	{
		Date dateTime = new Date();
		String name = RandomNameGenerator.generateName();
		Server server = Server.getRandom();
		Race race = Race.getRandom();
		Rank rank = Rank.getRandom();
		Archetype clazz = Archetype.getRandom();
		String location = "Yo mama";
		
		PlayerData p = new PlayerData(dateTime, name, server, race, rank, clazz, location);
		return p;
	}

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getServer() {
		return server.name();
	}



	public void setServer(Server server) {
		this.server = server;
	}



	public String getRank() {
		return rank.getRankTitle();
	}



	public void setRank(Rank rank) {
		this.rank = rank;
	}



	public String getRace() {
		return race.name();
	}



	public void setRace(Race race) {
		this.race = race;
	}



	public String getClazz() {
		return clazz.name();
	}



	public void setClazz(Archetype clazz) {
		this.clazz = clazz;
	}



	public String getDateTime() {
		return dateTime.toString();
	}



	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}



	public String getLocation() {
		return location;
	}



	public void setLocation(String location) {
		this.location = location;
	}


}

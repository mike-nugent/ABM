package gameinfo;

import java.util.Random;

public enum Archetype 
{
	Aethertech 	("aethertech.png"),
	Assassin 	("assassin.png"),
	Chanter		("chanter.png"),
	Cleric		("cleric.png"),
	Gladiator	("gladiator.png"),
	Gunslinger	("gunslinger.png"),
	Ranger		("ranger.png"),
	Songweaver	("songweaver.png"),
	Sorcerer	("sorcerer.png"),
	Spiritmaster("spiritmaster.png"),
	Templar		("templar.png"),
	Unknown		("unknown.png");
	
	private String _iconName;
	private Archetype(String icon)
	{ 
		_iconName = icon;
	}
	
	public String getIconName()
	{
		return _iconName;
	}

	public static Archetype getArchetype(String name, Server server, Race race) 
	{
		String clazz = PlayerClassFinder.findClass(name, server, race);
		try
		{
			return Archetype.valueOf(clazz);
		}
		catch (IllegalArgumentException e)
		{
			return Archetype.Unknown;
		}
	}
	
	public static Archetype fromString(String name) 
	{
		try
		{
			return Archetype.valueOf(name);
		}
		catch (IllegalArgumentException e)
		{
			return Archetype.Unknown;
		}
	}

	public static Archetype getRandom() 
	{
		Random random = new Random();
		return Archetype.values()[(random.nextInt(values().length -1))];
	}
}

package gameinfo;

import java.util.Random;
import java.util.Vector;

public enum Race 
{
	Asmodian	("A"),
	Elyos		("E"),
	Unknown		("?");

	private String _acronym;
	
	private Race(String acr)
	{
		_acronym = acr;
	}
	
	public String getAcyonym()
	{
		return _acronym;
	}
	
	public static Race getRace(String race) 
	{
		if(race == null)
			return Unknown;
		
		
		
		for(Race r : values())
		{
			if(r.getAcyonym().equals(race))
			{
				return r;
			}
		}
		
		try
		{
			Race r = Race.valueOf(race);
			return r;
		}
		catch (Exception e)
		{
			return Unknown;
		}
	}

	public static Race getRandom() 
	{
		Random random = new Random();
		return Race.values()[(random.nextInt(values().length -1))];
	}

	public static Vector<Race> getLegalValues() 
	{
		Vector<Race> returnLst = new Vector<Race>();
		for(Race s : values())
		{
			if(!s.equals(Unknown))
			{
				returnLst.add(s);
			}
		}
		
		return returnLst;
	}
}

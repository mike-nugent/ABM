package gameinfo;

import java.util.Random;

public enum Rank 
{
	I 		("5-Star Officer"),
	II 		("General"),
	III 	("Great General"),
	IV 		("Commander"),
	V		("Governor"),
	Unknown	("Unknown");
	
	private String _rankTitle;
	private Rank(String rankTitle)
	{
		_rankTitle =  rankTitle;
	}
	
	public String getRankTitle()
	{
		return _rankTitle;
	}
	
	public static Rank getRank(String rank)
	{
		for(Rank r : values())
		{
			if(r.name().equals(rank) || r.getRankTitle().equals(rank))
			{
				return r;
			}
		}		
		
		return Unknown;
	}

	public static Rank getRandom() 
	{
		Random random = new Random();
		return Rank.values()[(random.nextInt(values().length -1))];
	}
}

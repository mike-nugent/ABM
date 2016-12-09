package gameinfo;

import java.util.Random;

public enum Rank
{
    ZERO    ("Non Xform", 		"4s"),
	I 		("5-Star Officer",	"5s"),
	II 		("General", 		"GE"),
	III 	("Great General",	"GG"),
	IV 		("Commander",		"CO"),
	V		("Governor",		"GV"),
	Unknown	("Unknown",			"Unk");

	private String _rankTitle;
	private String _acronym;
	private Rank(final String rankTitle, final String acronym)
	{
		_rankTitle =  rankTitle;
		_acronym = acronym;
	}

	public String getRankTitle()
	{
		return _rankTitle;
	}
	
	public String getRankAcronym()
	{
		return _acronym;
	}


	public static Rank getRank(final String rank)
	{
		for(final Rank r : values())
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
		final Random random = new Random();
		return Rank.values()[(random.nextInt(values().length -1))];
	}
}

package gameinfo;

import java.util.Random;

public enum Rank
{
    ZERO    ("Non Xform"),
	I 		("5-Star Officer"),
	II 		("General"),
	III 	("Great General"),
	IV 		("Commander"),
	V		("Governor"),
	Unknown	("Unknown");

	private String _rankTitle;
	private Rank(final String rankTitle)
	{
		_rankTitle =  rankTitle;
	}

	public String getRankTitle()
	{
		return _rankTitle;
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

package loot;

public enum ItemQuality 
{
	MYTHIC(1), // purple
	EPIC(2), // orange
	UNIQUE(3), // gold?
	LEGEND(4), // blue
	RARE(5), // green
	COMMON(6), // white
	JUNK(7), // gray
	UNKNOWN(8); // unknown

	private int displayIndex;

	private ItemQuality(int index) 
	{
		this.displayIndex = index;
	}
	
	public int getIndex()
	{
		return displayIndex;
	}

	public static ItemQuality parse(String itemQuality) 
	{
		try
		{
			return valueOf(itemQuality);
		}
		catch (Exception e)
		{
			return UNKNOWN;
		}
	}
}

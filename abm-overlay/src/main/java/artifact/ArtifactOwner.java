package artifact;

import utils.Times;

public enum ArtifactOwner 
{
	Asmodian (Times.THIRTY_FIVE_MINUTES),
	Elyos (Times.THIRTY_FIVE_MINUTES),
	Balaur (Times.FIFTEEN_MINUTES),
	Unknown (-1);

	private long _time;
	private ArtifactOwner(long time)
	{
		_time = time;
	}
	

	public static ArtifactOwner getOwner(String owner) 
	{
		for(ArtifactOwner a : values())
		{
			if(a.name().equals(owner))
			{
				return a;
			}
		}
		
		return Unknown;
	}

	public long getCaptainSpawnCooldown() 
	{
		return _time;
	}
}

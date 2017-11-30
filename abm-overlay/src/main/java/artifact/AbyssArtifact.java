package artifact;

import utils.Times;

public enum AbyssArtifact 
{
	hellfire 	("Wildersage Outpost", 	 	Times.FIFTEEN_MINUTES),
	north 		("Dauntless Outpost", 		Times.TEN_MIUTES),
	left 		("Anchorbrak Outpost", 		Times.TEN_MIUTES),
	right 		("Brokenblade Outpost", 	Times.TEN_MIUTES),
	Unknown 	("?", 						-1);
	
	private String _label;
	private long _activationCooldown;
	private AbyssArtifact(String label, long activationCooldown)
	{
		_label = label;
		_activationCooldown = activationCooldown;
	}
	
	public long getActivationCooldown()
	{
		return _activationCooldown;
	}
	
	public String getActualName()
	{
		return _label;
	}

	public static AbyssArtifact getArtifact(String artifact) 
	{
		for(AbyssArtifact a : values())
		{
			if(a.getActualName().equals(artifact))
			{
				return a;
			}
		}
		
		return Unknown;
	}
}

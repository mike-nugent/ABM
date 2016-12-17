package handlers;

import java.util.Date;
import java.util.regex.Pattern;

import artifact.AbyssArtifact;
import artifact.ArtifactData;
import artifact.ArtifactOwner;
import main.MainSwing;

public class ArtifactCaptureLineHandler extends LineHandler 
{

	/**
	 * 	X + "of" + X + "has captured the" + X + "Artifact" + X)
		X + "Artifact has been lost to Balaur" + X)
		
		2015.12.10 21:16:14 : No Bacon For You of Asmodian has captured the Inferno I Artifact.
		2015.12.10 21:30:53 :   FRAGILE   of Elyos has captured the Miren's Gentle Breeze Artifact.
		2015.12.10 21:49:40 : No Bacon For You of Asmodian has captured the Hellfire Array Artifact.
		2015.12.10 21:49:40 : No Bacon For You of Asmodian has captured the Hellfire Array Artifact.
		2015.12.11 22:53:00 : Le Jardin Paradis of Asmodian has captured the Soul Destruction Artifact.
		2015.12.12 17:07:04 : Second Wind of Asmodian has captured the Chaos Rage Artifact.
		2015.12.12 17:11:56 :   Thug Life   of Asmodian has captured the Chaos Rage Artifact.

		2015.12.18 03:53:03 : The Inferno I Artifact has been lost to Balaur.
		2015.12.18 20:42:17 : The Hellfire Array Artifact has been lost to Balaur.
		2015.12.18 20:44:44 : The Inferno I Artifact has been lost to Balaur.
		2015.12.20 20:55:24 : The Krotan's Onslaught Artifact has been lost to Balaur.
		2015.12.20 20:55:24 : The Miren's Gentle Breeze Artifact has been lost to Balaur.
		2015.12.20 20:55:24 : The Miren's Gentle Breeze Artifact has been lost to Balaur.

	 */
	public ArtifactCaptureLineHandler() 
	{
		super( capture_arti, lose_arti);
	}
	
	public ArtifactData parseArtifactInfo(String line)
	{
		try 
		{
			System.out.println(line);
			String[] parsed = line.split(" "); 
			String date = parsed[0];
			String time = parsed[1];
			String legion, artifact,owner;
			
			if(line.contains("has been lost to Balaur"))
			{
				legion = "Balaur";
				artifact = line.substring(line.indexOf("The ") +4, line.indexOf(" Artifact has"));
				owner =  ArtifactOwner.Balaur.name();
			}
			else
			{
				legion = line.substring(line.indexOf(" : ") + 3, line.indexOf(" of "));
				artifact = line.substring(line.indexOf(" has captured the ") + 18, line.lastIndexOf(" Artifact"));
				owner = line.substring(line.indexOf(" of ") + 4, line.indexOf(" has captured "));		
			}

			
			Date T = null;
			try
			{
				T = formatter.parse(date + " " + time);
			} 
			catch (Exception e)
			{ 
				e.printStackTrace();
			}
			AbyssArtifact W = AbyssArtifact.getArtifact(artifact);
			ArtifactOwner O = ArtifactOwner.getOwner(owner);
			String L = "<"+legion+">";
			
			System.out.println(date + " " + time);
			System.out.println(legion);
			System.out.println(artifact);
			System.out.println(owner);

			ArtifactData data= new ArtifactData(T, W, O, L);
			return data;
		} 
		catch (Exception e) 
		{
			showError(e, line);
			return null;
		}
	}

	@Override
	protected void handleLine(String line, boolean isCurrent) 
	{
		ArtifactData data = parseArtifactInfo(line);
		System.out.println("TODO - handler for: " + data + " in " + this);

		//_main.artifactWasCaptured(data);
	}

}

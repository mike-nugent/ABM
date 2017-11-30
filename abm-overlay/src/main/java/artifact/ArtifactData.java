package artifact;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ArtifactData 
{
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	
	public Date date;
	public AbyssArtifact location;
	public ArtifactOwner owner;
	public String conqueringLegion;

	public ArtifactData(
			Date t, 
			AbyssArtifact w, 
			ArtifactOwner o,
			String legion)
	{
		this.date = t;
		this.location = w;
		this.owner = o;
		this.conqueringLegion = legion;
	}
	
	public static ArtifactData parseCaptureLine(String line)
	{
		System.out.println(line);
		String[] parsed = line.split(" "); 
		String date = parsed[0];
		String time = parsed[1];
		String legion, artifact,owner;
		
		if(line.contains("Artifact has been lost to Balaur"))
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

		return new ArtifactData(T, W, O, L);
	}
}
 
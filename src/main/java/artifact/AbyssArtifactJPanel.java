package artifact;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import gameinfo.IconLoader;

public class AbyssArtifactJPanel extends JPanel 
{
	static Map<AbyssArtifact, ArtifactTimerJPanel> artieMap = new HashMap<AbyssArtifact, ArtifactTimerJPanel>();
	
	public AbyssArtifactJPanel()
	{
		JLabel reshantaMap = new JLabel(IconLoader.reshanta);
		this.setLayout(null);
		this.add(reshantaMap);
		this.setBackground(Color.black);
		this.setSize(new Dimension(800, 800));
		reshantaMap.setSize(new Dimension(800, 800));
		
	
		ArtifactTimerJPanel krotonMiren = new ArtifactTimerJPanel(AbyssArtifact.north);
		krotonMiren.setLocation(250,130);  
		add(krotonMiren, 0);
		artieMap.put(AbyssArtifact.north, krotonMiren);
		
		ArtifactTimerJPanel mirenKysis = new ArtifactTimerJPanel(AbyssArtifact.right);
		mirenKysis.setLocation(600,385);  
		add(mirenKysis, 0);
		artieMap.put(AbyssArtifact.right, mirenKysis);

		ArtifactTimerJPanel kysisKroton = new ArtifactTimerJPanel(AbyssArtifact.left);
		kysisKroton.setLocation(185,545);  
		add(kysisKroton, 0);
		artieMap.put(AbyssArtifact.left, kysisKroton);

		ArtifactTimerJPanel hellfire = new ArtifactTimerJPanel(AbyssArtifact.hellfire);
		hellfire.setLocation(350,352);  
		add(hellfire, 0);
		artieMap.put(AbyssArtifact.hellfire, hellfire);
		
	}

	public void artifactWasCaptured(ArtifactData info) 
	{
		AbyssArtifact key = info.location;
		if(artieMap.containsKey(key))
		{
			ArtifactTimerJPanel panel = artieMap.get(key);
			panel.artifactCaptured(info);
		}
		else 
		{
			System.out.println("error, no key for " + key);
		}
	}
}
 
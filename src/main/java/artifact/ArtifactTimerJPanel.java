package artifact;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.security.acl.Owner;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gameinfo.IconLoader;
import utils.Times;

public class ArtifactTimerJPanel extends JPanel 
{
	private JLabel factionLabel;
	private JLabel legionLabl;
	private JLabel vulnLabel;
	private JLabel triggerLabel;
	private JLabel noData;
	private AbyssArtifact _artie;
	private ArtifactData _lastCaptureData;

	public ArtifactTimerJPanel(AbyssArtifact artie) 
	{
		_artie = artie;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setSize(new Dimension(150, 110));
		this.setBackground(Color.darkGray);

		factionLabel = makeLabel("", Color.CYAN, 9);
		legionLabl = makeLabel("", Color.CYAN, 15);
		vulnLabel = makeLabel("", Color.red, 20, IconLoader.artifactCaptain);
		triggerLabel = makeLabel("", Color.green, 20, IconLoader.artifactFire);
		noData = makeLabel("no data", Color.gray, 20);
		
		add(noData);
		
		add(factionLabel);
		add(legionLabl);
		add(vulnLabel);
		add(triggerLabel);
		
		factionLabel.setVisible(false);
		legionLabl.setVisible(false);
		vulnLabel.setVisible(false);
		triggerLabel.setVisible(false);


		Runnable r = new Runnable() 
		{
			public void run() 
			{
				try 
				{
					artieCountdownTimer();
				} 
				catch (Exception x) 
				{
					// in case ANY exception slips through
					x.printStackTrace();
				}
			}
		};

		Thread internalThread = new Thread(r);
		internalThread.start();
	}

	protected void artieCountdownTimer() 
	{
		try 
		{
			while (true) 
			{
				if(_lastCaptureData != null)
				{
					Date currentTime = new Date();
		    		long difference = currentTime.getTime() - _lastCaptureData.date.getTime();
		    		long vulnerableCooldown = _lastCaptureData.owner.getCaptainSpawnCooldown() - difference;
		    		long useableCooldown = _artie.getActivationCooldown() - difference;
		    		
		    		
		    		double scale = ((double) vulnerableCooldown / (double) _lastCaptureData.owner.getCaptainSpawnCooldown());
		    		double midLine = 200;
		    		int edge = (int) (255 - midLine);
		    		int gScale = (int) (midLine * scale);
		    		int rScale = (int) (midLine-(midLine * scale));
		    		
		    		int R = edge/2+rScale;
		    		int G = edge/2+gScale;
		    		int B = 0;
		    		
		    		if(R < 0 || R > 255) R=0;
		    		if(G < 0 || G > 255) G=0;
		    		if(B < 0 || B > 255) B=0;
		    		
		    				    		

		    		if(vulnerableCooldown < 0)
		    		{
		    			vulnerableCooldown = 0;
		    			vulnLabel.setForeground(Color.red);
		    			vulnLabel.setText("Vulnerable");

		    		}
		    		else
		    		{
			    		String vulnTimer = getMinSec(vulnerableCooldown);
		    			vulnLabel.setForeground(new Color(R, G, B));
			    		vulnLabel.setText(vulnTimer);
		    		}
		    		
		    		if(useableCooldown < 0)
		    		{
		    			useableCooldown = 0;
		    			triggerLabel.setForeground(Color.green);
		    			triggerLabel.setText("Ready");
		    		}
		    		else
		    		{
			    		String vulnTimer = getMinSec(useableCooldown);
			    		triggerLabel.setForeground(Color.orange);
			    		triggerLabel.setText(vulnTimer);
		    		}
		    		
		    		
		    		//only wait max of 1h on data. anything older is probably wrong
		    		if(difference > Times.ONE_HOUR)
		    		{
		    			_lastCaptureData = null;
		    		}
		    		
				}
				else
				{
					factionLabel.setVisible(false);
					legionLabl.setVisible(false);
					vulnLabel.setVisible(false);
					triggerLabel.setVisible(false);
					noData.setVisible(true);

				}
				Thread.sleep(1000);
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public AbyssArtifact getArtie() 
	{
		return _artie;
	}

	public void artifactCaptured(ArtifactData info) 
	{
		_lastCaptureData = info; 
		if(info.owner.equals(ArtifactOwner.Asmodian))
		{
			factionLabel.setForeground(Color.CYAN);
			legionLabl.setForeground(Color.CYAN);
		}
		else if(info.owner.equals(ArtifactOwner.Elyos))
		{
			factionLabel.setForeground(Color.green);
			legionLabl.setForeground(Color.green);
		}
		else
		{
			factionLabel.setForeground(Color.pink);
			legionLabl.setForeground(Color.pink);
			triggerLabel.setVisible(false);
		}
			
		factionLabel.setText(info.owner.name());
		legionLabl.setText(info.conqueringLegion);
		
		factionLabel.setVisible(true);
		legionLabl.setVisible(true);
		vulnLabel.setVisible(true);
		triggerLabel.setVisible(true);
		noData.setVisible(false);
	}

	private JLabel makeLabel(String txt, Color color, int size, ImageIcon icon) 
	{
		JLabel label = new JLabel(txt, icon, JLabel.LEFT);
		return update(label, color, size);
	}

	private JLabel makeLabel(String txt, Color color, int size) 
	{
		JLabel label = new JLabel(txt);
		return update(label, color, size);
	}

	private JLabel update(JLabel label, Color color, int size) 
	{
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD + Font.ITALIC, size));
		label.setForeground(color);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		return label;
	}
	
	public String getMinSec(long millisec) 
	{
		String ms = String.format("%02d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(millisec)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisec)),
				TimeUnit.MILLISECONDS.toSeconds(millisec)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisec)));
		return ms;
	}

}

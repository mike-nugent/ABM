package xforms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import gameinfo.PlayerData;
import gameinfo.Race;
import gameinfo.Rank;
import gameinfo.Server;
import utils.WrapLayout;

public class XFormDisplayJPanel extends JPanel 
{
	Map<Rank, JPanel> panels = new HashMap<Rank, JPanel>();
	private Server _server;
	private Race _race;	
	
	private JPanel cooldownPanel;
	
	public XFormDisplayJPanel(Server server, Race race)
	{
		super();
		_server = server;
		_race = race; 
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		panels.put(Rank.I, makeBorderPanel(Rank.I));
		panels.put(Rank.II, makeBorderPanel(Rank.II));
		panels.put(Rank.III, makeBorderPanel(Rank.III));
		panels.put(Rank.IV, makeBorderPanel(Rank.IV));
		panels.put(Rank.V, makeBorderPanel(Rank.V));

		cooldownPanel = makeBorderPanel(Rank.Unknown);
	}
	
	private JPanel makeBorderPanel(Rank rank) 
	{
		JPanel panel = new JPanel(new WrapLayout());
		panel.setBackground(Color.white);
		panel.setBorder(makeBorder(rank));
		this.add(panel);
		panel.setVisible(false);
		return panel;
	}
	
	private Border makeBorder(Rank rank) 
	{ 
		String txtString;
		if(rank.equals(Rank.Unknown))
		{
			txtString = "Players with transform on cooldown: ";
		}
		else
		{
			txtString  = rank.getRankTitle() + "s with active transform: ";
		}
			return BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.white), 
					txtString,
					TitledBorder.LEFT,
					TitledBorder.DEFAULT_POSITION,
					new Font(Font.SERIF,Font.ITALIC, 30));

	}
	

	public synchronized boolean checkDeath(PlayerData data) 
	{			
		JPanel panel = panels.get(data.rank);

		if(panel.getComponentCount() > 0)
		{
			for(Component c : panel.getComponents())
			{
				if(c instanceof TransformBar)
				{
					TransformBar xform = (TransformBar) c;
					if(xform.getPlayerName().equals(data.name) &&
							xform.getPlayerServer().equals(data.server) &&
							xform.getPlayerRace().equals(data.race) &&
							xform.isPlayerAlive())
					{
						xform.hasDied(true);
						return true;
					}	
				}
			}
		}

		
		return false;
	}

	public void addXform(TransformBar xform) 
	{
		JPanel panel = panels.get(xform.getPlayerRank());
		
		if(xform.isPlayerAlive())
		{
			panel.add(xform);
			if(!panel.isVisible())
			{
				panel.setVisible(true);
			}
		}
		else
		{
			cooldownPanel.add(xform);
			if(!cooldownPanel.isVisible())
			{
				cooldownPanel.setVisible(true); 
			}
		}
	}

	public void transformDied(TransformBar transformBar) 
	{
		JPanel panel = panels.get(transformBar.getPlayerRank());
		panel.remove(transformBar);
		cooldownPanel.add(transformBar);
		if(!cooldownPanel.isVisible())
		{
			cooldownPanel.setVisible(true); 
		}
		
		if(panel.getComponentCount() == 0)
		{
			panel.setVisible(false); 
		}
	}
	
	
	public void cooldownFinished(TransformBar transformBar) 
	{
		cooldownPanel.remove(transformBar);
		if(cooldownPanel.getComponentCount() == 0)
		{ 
			cooldownPanel.setVisible(false);
		}
	}

	public CharSequence getTabLabelInfo() 
	{
		return _server.getServerString() + "-" + _race.getAcyonym();
	}

	public int getNumActiveXforms() 
	{
		int count = 0;
		for(JPanel p : panels.values())
		{
			count += p.getComponentCount();
		}
		return count;
	}
	
	public int getNumCooldownXforms() 
	{
		int count = 0;
		count += cooldownPanel.getComponentCount();
		return count;
	}

	public boolean isAnythingDisplayed() 
	{
		if(getNumActiveXforms() + getNumCooldownXforms() > 0)
		{ 
			System.out.println(getNumActiveXforms() + " " + getNumCooldownXforms());
			return true;
		}
		else
		{
			return false;
		}
	}


}

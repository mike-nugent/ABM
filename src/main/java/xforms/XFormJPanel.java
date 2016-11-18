package xforms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import gameinfo.PlayerData;
import gameinfo.Race;
import gameinfo.Server;
import main.Main;
import sounds.SoundManager;

public class XFormJPanel extends JPanel 
{
	private JTabbedPane tabbedPane;
	private Main _main;
	private Map<String, XFormDisplayJPanel> transformPanels  = new HashMap<String, XFormDisplayJPanel>();
	private JLabel helpTxt;

	public XFormJPanel(Main main) 
	{
		super();
		_main = main;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(Color.gray);
		tabbedPane.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);
		helpTxt = new JLabel("Content on this tab will appear when people use their Transform: Guardian General abilities.");
		helpTxt.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
		this.add(helpTxt);
		
		this.add(tabbedPane);		
	}



	
	public synchronized XFormDisplayJPanel getOrAddPanel(Server server, Race race)
	{
		if(helpTxt != null)
		{
			remove(helpTxt);
			helpTxt = null;
		}
		
		String key = server.getServerString()+"-"+race.getAcyonym();
		if(transformPanels.containsKey(key))
		{
			return transformPanels.get(key);
		}
		else
		{
			System.out.println("Creating KEY " + key + " thread " +  Thread.currentThread().getId());
			XFormDisplayJPanel newPanel = new XFormDisplayJPanel(server, race);
			
			JPanel wrap = wrapWithScroller(newPanel);
			wrap.setAlignmentX(Component.LEFT_ALIGNMENT);

			tabbedPane.add(key , wrap);
			transformPanels.put(key, newPanel);
			System.out.println("Creating panel done");
			return newPanel;
		}
	}
	


	private synchronized XFormDisplayJPanel getPanelOrNull(Server server, Race race) 
	{
		String key = server.getServerString()+"-"+race.getAcyonym();
		return transformPanels.get(key);
	}
	
	


	private JPanel wrapWithScroller(JPanel panel) 
    {
        JPanel borderLayoutPanel = new JPanel(new BorderLayout());
        JScrollPane scroll = new JScrollPane(
        		panel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        borderLayoutPanel.add(scroll, BorderLayout.CENTER);
        return borderLayoutPanel;
    }

	public synchronized void  addNewTransform(PlayerData data) 
	{
		TransformBar xform = new TransformBar(_main, data);
		XFormDisplayJPanel panelToAdd = getOrAddPanel(data.server, data.race);
		panelToAdd.addXform(xform);
		updateTabLabel(panelToAdd);
		SoundManager.playTickSound();
		System.out.println("Adding for" + data.name + " " + data.dateTime);
	}


	
	public synchronized void transformDied(TransformBar transformBar) 
	{
		XFormDisplayJPanel panel = getOrAddPanel(transformBar.getPlayerServer(), transformBar.getPlayerRace());
		panel.transformDied(transformBar);
		updateTabLabel(panel);
	}

	public synchronized void cooldownFinished(TransformBar transformBar) 
	{
		XFormDisplayJPanel panel = getOrAddPanel(transformBar.getPlayerServer(), transformBar.getPlayerRace());
		panel.cooldownFinished(transformBar);
		
		updateTabLabel(panel);
	}
	
	private synchronized void updateTabLabel(XFormDisplayJPanel panelToAdd) 
	{
	    int count = tabbedPane.getTabCount();
	    for (int i = 0; i < count; i++) 
	    {
	      String label = tabbedPane.getTitleAt(i);
	      if(label.contains(panelToAdd.getTabLabelInfo()))
	      {
	    	  String newLabel = panelToAdd.getTabLabelInfo() + " ( " + panelToAdd.getNumActiveXforms()  + " ) ";
	    	  tabbedPane.setTitleAt(i, newLabel);
	    	  return;
	      }
	    }
	}


	public synchronized boolean checkDeath(PlayerData data) 
	{
		XFormDisplayJPanel panel = getPanelOrNull(data.server, data.race);
		if(panel == null)
		{
			return false;
		}
		else
		{
			return panel.checkDeath(data);
		}
	}




	public int getNumActiveXforms() 
	{
		int totalCount = 0;
		for(Server s : Server.values())
		{
			if(!s.equals(Server.Unknown))
			{
				
				XFormDisplayJPanel asmoPanel = getPanelOrNull(s, Race.Asmodian);
				XFormDisplayJPanel elyPanel = getPanelOrNull(s, Race.Elyos);
				
				if(asmoPanel != null)
				{
					totalCount += asmoPanel.getNumActiveXforms();
				}
				if(elyPanel != null)
				{
					totalCount += elyPanel.getNumActiveXforms();
				}
			}
		}
		
		return totalCount;
	}
}

package areas;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import config.ConfigFile;
import gameinfo.AbilityData;
import gameinfo.Race;
import utils.WrapLayout;

public class TeamJPanel extends JPanel
{
	protected JPanel enemyTeamPanel;
	protected JPanel yourTeamPanel;
	protected Map<String, PlayerAbilityWatcher> map = new HashMap<String, PlayerAbilityWatcher>();
	
	public TeamJPanel()
	{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel enemyWrapper = makeBorderPanel("Enemy Team");
		JPanel yourWrapper = makeBorderPanel("Your Team");
		
		enemyTeamPanel = new JPanel();
		enemyTeamPanel.setLayout(new BoxLayout(enemyTeamPanel, BoxLayout.PAGE_AXIS));
		
		yourTeamPanel = new JPanel();
		yourTeamPanel.setLayout(new BoxLayout(yourTeamPanel, BoxLayout.PAGE_AXIS));


		enemyWrapper.add(enemyTeamPanel);
		yourWrapper.add(yourTeamPanel);
	}
	
	public void makePlayer(String name, Race race) 
	{
		PlayerAbilityWatcher playerWatcher = new PlayerAbilityWatcher(name);
		map.put(name, playerWatcher);
		
		if(ConfigFile.getRace().equals(race))
		{
			//Same team
			yourTeamPanel.add(playerWatcher);
			yourTeamPanel.add(Box.createVerticalStrut(20));
		}
		else
		{
			//Enemy team
			enemyTeamPanel.add(playerWatcher);
			enemyTeamPanel.add(Box.createVerticalStrut(20));
		}
	}
	
	public void updateAbility(AbilityData data) 
	{
		PlayerAbilityWatcher watcher = map.get(data.caster);
		watcher.addNewAbility(data);
	}

	private JPanel makeBorderPanel(String text) 
	{ 
		JPanel panel = new JPanel(new WrapLayout(FlowLayout.LEFT));
		panel.setBackground(Color.white);
		panel.setBorder(makeBorder(text));
		this.add(panel);
		return panel;
	}
	
	private Border makeBorder(String text) 
	{ 
			return BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.white), 
					text,
					TitledBorder.LEFT,
					TitledBorder.DEFAULT_POSITION,
					new Font(Font.SERIF,Font.ITALIC, 30));
	}
	
}

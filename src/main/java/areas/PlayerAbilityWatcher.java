package areas;

import java.awt.Font;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gameinfo.AbilityData;
import gameinfo.IconLoader;

public class PlayerAbilityWatcher extends JPanel
{
	private String _name;
			
	public PlayerAbilityWatcher(String name)
	{
		_name = name;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setAlignmentX(LEFT_ALIGNMENT);
		JLabel player = new JLabel(name, IconLoader.aethertech, SwingConstants.LEFT);
		player.setFont(new Font(Font.MONOSPACED,Font.BOLD, 22));
		this.add( Box.createHorizontalGlue() );

		this.add(player);
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void addNewAbility(AbilityData data)
	{
		System.out.println("adding "+ data.ability);
		this.add(new AbilityBar(this, data.ability, new Date())); 
	}
}

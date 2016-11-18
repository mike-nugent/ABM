package areas;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import gameinfo.AbilityData;
import gameinfo.IconLoader;
import gameinfo.Race;
import gameinfo.Rank;
import sounds.SoundManager;
import utils.WrapLayout;

public class IdgelDomeJPanel extends TeamJPanel implements ActionListener
{
	public IdgelDomeJPanel()
	{
		super();
		//TODO - junk test code, remove this!
		JButton btn = new JButton("sound");
		btn.addActionListener(this);
	//	add(btn);
		
	}

	public void abilityDetected(AbilityData data) 
	{
		if(!map.containsKey(data.caster))
		{
			makePlayer(data.caster, Race.Unknown);
		}
		
		updateAbility(data);
	}

	public void actionPerformed(ActionEvent e) 
	{
		SoundManager.playTransformSound();
	}


}

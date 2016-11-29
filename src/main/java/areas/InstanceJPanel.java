package areas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import gameinfo.AbilityData;
import main.MainSwing;

public class InstanceJPanel extends JPanel
{
	private MainSwing _main;
	private JTabbedPane tabbedPane;
	private IdgelDomeJPanel idPanel;
	private DDPanel ddPanel;

	public InstanceJPanel(MainSwing main)
	{
		super(); 
		_main = main;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
 
		tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(Color.gray);
		tabbedPane.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);
		
		idPanel = new IdgelDomeJPanel();
		ddPanel = new DDPanel();
		tabbedPane.addTab("Open World", new JPanel()); 
		tabbedPane.addTab("Darkenspire Depths", wrapWithScroller(ddPanel));
		tabbedPane.addTab("Idgel Dome", wrapWithScroller(idPanel));
		tabbedPane.addTab("Kamar Battlefield", new JPanel());
		tabbedPane.addTab("Engulphed Ophidian Bridge", new JPanel());
		tabbedPane.addTab("Iron Wall Warfront", new JPanel());
		tabbedPane.addTab("Tereth Dredgeon", new JPanel());
		tabbedPane.addTab("Discipline Arena", new JPanel());
		tabbedPane.addTab("Harmony Arena", new JPanel());
		tabbedPane.addTab("Chaos Arena", new JPanel());
		tabbedPane.addTab("Glory Arena", new JPanel());

		tabbedPane.setSelectedIndex(1);
		this.add(tabbedPane);		
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

	public void abilityDetected(AbilityData data) 
	{
		idPanel.abilityDetected(data);

	}
}

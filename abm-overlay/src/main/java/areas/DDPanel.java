package areas;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import utils.Times;
import utils.WrapLayout;

public class DDPanel extends JPanel
{

	public DDPanel() 
	{
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		
		
		//Lava spirits page
		//Trigger timer: 2016.05.20 23:49:06 : The Lava Protector and Heatvent Protector are sharing the Fount.
		JPanel lavaSpiritsPanel = makeLavaSpiritPanel();

		
		//Orissan page
		//2016.05.21 00:02:16 : Reverted Orissan: Do not expect mercy from Orissan! 
		//(note) I think this happens soon after you first engage, not right away.
		
		/*
		    2015.12.08 19:50:32 : Orissan begins to Ascend through Ascension Dominance.
			2015.12.08 19:56:04 : Orissan has become exhausted by Ascension Dominance.
			05:32
			
			2016.05.07 00:12:07 : Orissan begins to Ascend through Ascension Dominance.
			2016.05.07 00:17:37 : Orissan has become exhausted by Ascension Dominance.
			05:30
			
			2016.05.14 00:21:22 : Orissan begins to Ascend through Ascension Dominance.
			2016.05.14 00:26:49 : Orissan has become exhausted by Ascension Dominance.
			05:27
			
			2016.05.21 00:02:16 : Orissan begins to Ascend through Ascension Dominance.
			2016.05.21 00:07:46 : Orissan has become exhausted by Ascension Dominance.
			05:30
			
			On average, can start the clock at msg1 and at 05:30, start the dps.
			
			After start message, is 01:47 before stage 2
			After stage 2 messgae, is 01:47 before stage 3
			After stage 3 message, is 01:54 before stage 4 (burn stage).
			
			Breakdown in times:
			See this message: Orissan begins to Ascend through Ascension Dominance.  Start the timer.  Ignore any other of these messages you see.
			at 01:47 Stage 2 starts.  Make a alarm sound so the user knows.
			at 03:35 Stage 3 starts.  make a alarm sound so the user knows.
			at 05:30 Stage 4 starts.  this is burn stage.
			
			You then have 03:40 seconds to burn the boss.  It needs to die in this time frame
			So the timer should go until 09:10 and then be over.  lets make a count down!
			
			
			
		 */
		JPanel orissanPanel = makeOrissanPanel();

		
		//Protect ladies page
		JPanel protectLadiesPanel = makeLadiesPanel();
		
		
		//Beritra page
		//Trigger timer: 2016.05.21 00:36:01 : Beritra transforms into a dragon.
		JPanel beritraPanel = makeBeritraPanel();


		
		JTabbedPane pane = new JTabbedPane();
		pane.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
		pane.addTab("Lava Spirits", lavaSpiritsPanel);
		pane.addTab("Orissan", orissanPanel);
		pane.addTab("Lady Protection",protectLadiesPanel);
		pane.addTab("Beritra", beritraPanel);
		add(pane);
	}
	


	private JPanel makeBeritraPanel() 
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JPanel top = makePanel(panel);
		JPanel desc = makePanel(panel);
		JPanel center = makePanel(panel);
		makePanel(panel);
		makePanel(panel);
		
		top.add(makeTitle("Final Fight: Beritra"));
		desc.add(makeDescription("Things to note:\n"
				+ "When starting the encounter, the normal version of beritra has 3 buffs.\n"
				+ "To remove them, the Siel Protectors must be defeated and the player\n"
				+ "who recieves the debuff (name green & character glows) will need to stand\n"
				+ "next to Beritra and explode on him (standing at least 6m from the tank)\n"
				+ "The first person to damage the Siel Protector will get the debuff.\n\n"
				+ "During the final stage, the Meele group stands under the left claw, magic \n"
				+ "under the right. At the intervals below, 3 more Siel Protectors will spawn \n"
				+ "and need to be handled in the same way. (Standing at Dragon Beritra's face).\n"
				+ "When Siel Protectors spawn, EVERYONE should jump out of the center area\n\n"
				+ "Green & Purple mobs are kited by the Templar, Blue mobs too at burn stage"));
		
		CountUpTimer beritraTimer = new CountUpTimer(
				  "After dragon form Beritra spawns, start the timer:\n"
				+ "01:00 - First Round,  Seal Protector spawns\n"
				+ "03:30 - Second Round, Seal Protector spawns\n"
				+ "06:00 - Third Round,  Seal Protector spawns\n"
				+ "(06:15 - 08:00) - BURN STAGE after thrid round.\n"
				+ "Beritra must be killed by 08:00 (less time if not all ladies saved)");
		center.add(beritraTimer);
		
		
		Map<String, String> map = new HashMap<String, String>();

		String B = "B";
		String A = "A";
		
		//Wave 1
		map.put("00:10", B);
		map.put("00:20", B);
		map.put("00:30", B);
		map.put("00:40", B);
		map.put("00:43", B);
		map.put("00:46", B);
		map.put("00:49", B);
		map.put("00:50", B);
		map.put("00:51", B);
		map.put("00:52", B);
		map.put("00:53", B);
		map.put("00:54", B);
		map.put("00:55", A);
		
		
		//wave 2
		map.put("02:40", B);
		map.put("02:50", B);
		map.put("03:00", B);
		map.put("03:10", B);
		map.put("03:13", B);
		map.put("03:16", B);
		map.put("03:19", B);
		map.put("03:20", B);
		map.put("03:21", B);
		map.put("03:22", B);
		map.put("03:23", B);
		map.put("03:24", B);
		map.put("03:25", A);
	
		
		//Wave 3
		map.put("05:10", B);
		map.put("05:20", B);
		map.put("05:30", B);
		map.put("05:33", B);
		map.put("05:36", B);
		map.put("05:39", B);
		map.put("05:40", B);
		map.put("05:50", B);
		map.put("05:51", B);
		map.put("05:52", B);
		map.put("05:53", B);
		map.put("05:54", B);
		map.put("05:55", A);
		
		
		//BURN STAGE
		map.put("06:30", B);
		map.put("07:00", B);
		map.put("07:10", B);
		map.put("07:20", B);
		map.put("07:30", B);
		map.put("07:35", B);
		map.put("07:40", B);
		map.put("07:45", B);
		map.put("07:50", B);
		map.put("07:51", B);
		map.put("07:52", B);
		map.put("07:53", B);
		map.put("07:54", B);
		map.put("07:55", B);
		map.put("07:56", B);
		map.put("07:57", B);
		map.put("07:58", B);
		map.put("07:59", B);
		map.put("08:00", B);

		
		
		beritraTimer.setSoundMap(map);
		
		return panel;
	}



	private JPanel makeLadiesPanel() 
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setAlignmentX(LEFT_ALIGNMENT);
		JPanel top = makePanel(panel);
		JPanel desc = makePanel(panel);
		JPanel center = makePanel(panel);
		makePanel(panel);
		makePanel(panel);
		top.add(makeTitle("Fight 3: Lady Protection"));
		
		desc.add(makeDescription("Things to note:\n\n"
				+ "keep as many NPC as possible alive because each NPC saved increases the \n"
				+ "allocated time for Berira by 15s.\n\n"
				+ "The chanter must buff every NPC\n\n"
				+ "Use marks on each of the NPC's. Use them to idenfify which NPC to heal\n\n"
				+ "There are 5 waves in hard mode:\n\n"
				+ "1) - not important, kill\n\n"
				+ "2) - Sorcerer, casts AE sleeps\n\n"
				+ "3) - not important, kill\n\n"
				+ "4) - Cleric, heals the mobs so kill it first\n\n"
				+ "5) - Commander Vritsha\n"
				+ "Have the templar pull the Command away from the mobs.  Burn mobs first, \n"
				+ "kill Vritsha after all mobs are dead"));
		
		
		return panel;	
	}



	private JPanel makeOrissanPanel() 
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JPanel top = makePanel(panel);
		JPanel desc = makePanel(panel);
		JPanel center = makePanel(panel);
		makePanel(panel);
		makePanel(panel);
				
		top.add(makeTitle("Fight 2: Orissan"));
		desc.add(makeDescription(
				  "Summon Freezing Crystal\n"
				+ "Ice sheets appears on the ground and crystals\n"
				+ "or mines replace them a few seconds later.\n\n"
				
				+ "Rigid Gait\n"
				+ "Selects one of the 4 crystals and sends a wave which destroys the crystal \n"
				+ "and inflicts water damage to anyone touched by the wave. Cast 2s.\n\n"
				
				+ "Ice Explosion\n"
				+ "AoE with a 70m range around the boss exploding all mines. Cast 2s.\n\n"
				
				+ "Ereshkigal's Protection\n"
				+ "A shield that reflects 3x the damage. This is cast 2 minutes after dps starts.\n"
				+ "(at the 07:00 mark)  \n"
				+ "Lasts for 5 seconds, or can be removed by a SpiritMaster."));
		
		/*
            at 01:47 Stage 2 starts.  Make a alarm sound so the user knows.
			at 03:35 Stage 3 starts.  make a alarm sound so the user knows.
			at 05:30 Stage 4 starts.  this is burn stage.
			
			You then have 03:40 seconds to burn the boss.  It needs to die in this time frame
			So the timer should go until 09:10 and then be over.  lets make a count down!
		 */
		CountUpTimer orissanTimer = new CountUpTimer(
				"00:00 - start Invulnerable\n" + 
				"01:47 - start Vulnerable\n" +
				"03:35 - start Invulnerable\n" +
			    "05:30 - start Vulnerable (burn stage)\n" +
				"09:10 - CAP TIME LIMIT");
		center.add(orissanTimer);
		
		
		Map<String, String> map = new HashMap<String, String>();

		String B = "B";
		String A = "A";
		
		//Wave 1
		map.put("00:10", B);
		map.put("00:11", B);
		map.put("00:12", B);
		map.put("00:13", B);
		map.put("00:14", B);
		map.put("00:15", B);
		
		map.put("01:40", B);
		map.put("01:42", B);
		map.put("01:43", B);
		map.put("01:44", B);
		map.put("01:45", B);
		map.put("01:46", B);
		map.put("01:47", B);

		map.put("03:30", B);
		map.put("03:31", B);
		map.put("03:32", B);
		map.put("03:33", B);
		map.put("03:34", B);
		map.put("03:35", B);
		
		map.put("04:30", B);
		map.put("04:40", B);
		map.put("04:50", B);
		map.put("05:00", B);
		map.put("05:10", B);
		map.put("05:15", B);
		map.put("05:20", B);
		map.put("05:25", B);
		map.put("05:26", B);
		map.put("05:27", B);
		map.put("05:28", B);
		map.put("05:29", B);
		map.put("05:30", A);

		
		map.put("08:30", B);
		map.put("08:32", B);
		map.put("08:34", B);
		map.put("08:36", B);
		map.put("08:38", B);
		map.put("08:40", B);
		map.put("08:42", B);
		map.put("08:44", B);
		map.put("08:46", B);
		map.put("08:48", B);
		map.put("08:50", B);
		map.put("08:52", B);
		map.put("08:54", B);
		map.put("08:56", B);
		map.put("08:58", B);
		map.put("09:00", B);
		map.put("09:01", B);
		map.put("09:02", B);
		map.put("09:03", B);
		map.put("09:04", B);
		map.put("09:05", B);
		map.put("09:06", B);
		map.put("09:07", B);
		map.put("09:08", B);
		map.put("09:09", B);
		map.put("09:10", A);

		
		orissanTimer.setSoundMap(map);
		
		
		
		return panel;
	}



	private JPanel makeLavaSpiritPanel() 
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setAlignmentX(LEFT_ALIGNMENT);
		JPanel top = makePanel(panel);
		JPanel desc = makePanel(panel);
		JPanel center = makePanel(panel);
		makePanel(panel);
		makePanel(panel);
		top.add(makeTitle("Fight 1: Lava Spirits"));
		
		desc.add(makeDescription(
				  "Sweltering Heat Bind\n"
				+ "DoT inflicting big damage after 3s. Cast 2s. - MUST BE DISPELLED ASAP"));
		
		CountDownTimer spiritsTimer = new CountDownTimer(Times.FIVE_MINUTES,
				"05:00 - To kill both lava mobs\n"
			  + "Both bosses must be killed within 15 seconds of eachother");
		center.add(spiritsTimer);
		
		
		
		Map<String, String> map = new HashMap<String, String>();

		String B = "B";
		String A = "A";
		
		//Wave 1
		map.put("04:30", B);
		map.put("04:00", B);
		map.put("03:30", B);
		map.put("03:00", B);
		map.put("02:30", B);
		map.put("02:00", B);
		map.put("01:30", B);
		map.put("01:00", B);
		map.put("00:30", B);
		
		map.put("00:25", B);
		map.put("00:20", B);
		map.put("00:15", B);
		map.put("00:10", B);
		map.put("00:09", B);
		map.put("00:08", B);
		map.put("00:07", B);
		map.put("00:06", B);
		map.put("00:05", B);
		map.put("00:04", B);
		map.put("00:03", B);
		map.put("00:02", B);
		map.put("00:01", B);
		map.put("00:00", A);

		
		spiritsTimer.setSoundMap(map);
		
		
		
		
		return panel;
	}






	private JTextPane makeDescription(String string) 
	{
		JTextPane area = new JTextPane();
		area.setAlignmentX(LEFT_ALIGNMENT);
		area.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		area.setText(string);
		area.setEditable(false);
		return area;
	}

	private JLabel makeTitle(String string) 
	{
		JLabel area = new JLabel(string);
		area.setAlignmentX(LEFT_ALIGNMENT);
		area.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
		return area;
	}


	private JPanel makePanel(JPanel panel) 
	{
		JPanel panel3 = new JPanel(new WrapLayout());
		panel3.setAlignmentX(LEFT_ALIGNMENT);
		//panel3.setBorder(BorderFactory.createLineBorder(Color.black));
		panel3.setBackground(Color.white);
		panel.add(panel3);
		return panel3;
	}

}

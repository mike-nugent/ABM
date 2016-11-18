package xforms;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import config.ConfigFile;
import gameinfo.Archetype;
import gameinfo.IconLoader;
import gameinfo.PlayerData;
import gameinfo.Race;
import gameinfo.Rank;
import gameinfo.Server;
import main.Main;

public class TransformBar extends JPanel 
{
	public static final Color ENEMY_COLOR =  new Color(51,125,61);
	public static final Color FRIENDLY_COLOR =  new Color(29,78,145); 
	private static final long TEN_MINUTES = 600000;
	public static final long TWO_HOURS = 7200000;
	private JLabel timeTxt;  
	private JLabel rankTxt;
	private JLabel nameTxt;
	private JLabel raceTxt;
	private Date _transformStartTime;
	private boolean _isAlive;
	private boolean _coolDownThreadStarted = false;
	private String _name;
	private Race _race;
	private Server _server;
	private Rank _rank;
	private Archetype _clazz;
	private Main _main;

	
	public TransformBar(Main main, PlayerData playerData) 
	{
		_isAlive = true;
		_main = main;
		_name = playerData.name;
		_race = playerData.race;
		_server = playerData.server;
		_rank = playerData.rank;
		_clazz = playerData.clazz;
		 
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBorder(new EmptyBorder(5,5,5,10));
		
		raceTxt  = new JLabel(_server.name() + " " + _race.name());
		raceTxt.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		if(_race.equals(Race.Asmodian))
		{
			raceTxt.setForeground(FRIENDLY_COLOR);
		}
		else
		{
			raceTxt.setForeground(ENEMY_COLOR);
		}

		
		this.add(raceTxt);
		
		rankTxt = new JLabel(_rank.getRankTitle());
		rankTxt.setForeground(Color.gray);
		this.add(rankTxt);
		
		
		nameTxt = new JLabel(_name);
		nameTxt.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 18));
		updateBasedOnFriendlyOrFoe( nameTxt, Color.black, Color.red);

		
		nameTxt.setIcon(IconLoader.getIcon(_clazz)); 
		this.add(nameTxt);
		
		_transformStartTime = playerData.dateTime;
		// to test randomness transformStartTime.setTime((long) (transformStartTime.getTime() - Math.random() * 600000));
		timeTxt = new JLabel(getMinSec(_transformStartTime.getTime()));
		timeTxt.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		this.add(timeTxt);
		

		 Runnable r = new Runnable() 
		 {
		      public void run() 
		      {
		        try 
		        {
		        	xformEffectTimer();
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

	public void hasDied(boolean hasDied)
	{
		_isAlive = false;
		if(!_isAlive)
		{
			updateBarToInactive();
		}
	}

	private void updateBasedOnFriendlyOrFoe(JLabel label, Color friendlyColor, Color enemyColor) 
	{
		if(_race.equals(ConfigFile.getRace()))
		{
			label.setForeground(friendlyColor);
		}
		else
		{
			label.setForeground(enemyColor);
		}		
	}
 
	protected void xformEffectTimer() 
	{
		try
		{  
	    	while(isPlayerAlive())
	    	{  
	    		Date currentTime = new Date();
	    		long difference = currentTime.getTime() - _transformStartTime.getTime();
	    		long countDown = TEN_MINUTES - difference;
	    		if(countDown<0)
	    		{
	    			countDown = 0;
	    		}
	    		
	    		double scale = ((double) countDown / (double) TEN_MINUTES);
	    		double midLine = 150;
	    		int edge = (int) (255 - midLine);
	    		int gScale = (int) (midLine * scale);
	    		int rScale = (int) (midLine-(midLine * scale));

	    		String diffString = getMinSec(countDown);
	    		timeTxt.setText(diffString);
	    		
	    		int R = edge/2+rScale;
	    		int G = edge/2+gScale;
	    		int B = 0;
	    		
	    		if(R < 0 || R > 255) R=0;
	    		if(G < 0 || G > 255) G=0;
	    		if(B < 0 || B > 255) B=0;
	    		
	    		timeTxt.setForeground(new Color(R, G, B));
	    		
	    		if(countDown <= 0)
	    		{
	    			updateBarToInactive();
	    			return;
	    		}
	    		
	    		Thread.sleep(1000); 
	    	}
		} catch (Exception e)
		{ 
			e.printStackTrace();
		}
	}

	
	private void updateBarToInactive() 
	{ 
		System.out.println("has died, moving to inactive");
		this.setBackground(Color.darkGray);
		updateBasedOnFriendlyOrFoe(nameTxt, Color.lightGray, Color.pink);
		raceTxt.setForeground(Color.lightGray);
		rankTxt.setForeground(Color.lightGray);
		
		this.remove(timeTxt);
		timeTxt = new JLabel("00:00:00");
		timeTxt.setForeground(Color.pink);
		timeTxt.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		this.add(timeTxt);
		
		_main.moveBarToInactive(this);
		startThreadForCooldownTimer();		
	}

	public boolean isPlayerAlive() 
	{
		return _isAlive ;
	}

	private void startThreadForCooldownTimer() 
	{
		if(_coolDownThreadStarted)
		{
			return;
		}
		else
		{
			_coolDownThreadStarted = true;
		}
		
		
		 Runnable r = new Runnable() 
		 {
		      public void run()
		      {
		        try 
		        {
		        	//sleep for one second to allow the other thread to finish if it is running still.
		        	Thread.sleep(1000);
		        	startCooldownTimer();
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
	
	private void startCooldownTimer() 
	{
		try
		{  
	    	while(true)
	    	{ 
	    		Date currentTime = new Date();
	    		long difference = currentTime.getTime() - _transformStartTime.getTime();
	    		long cooldownLeft = TWO_HOURS - difference;
	    		String cooldown = getHrMinSec(cooldownLeft);
	    		timeTxt.setText(cooldown);
	        	if(!timeTxt.getForeground().equals(Color.pink))
	        	{
		        	timeTxt.setForeground(Color.pink);
	        	}

	    		if(cooldownLeft <= 0)
	    		{
	    			System.out.println("Cooldown for "+ getPlayerName() + " is done");
	    			_main.removeCooldown(this);
	    			return;
	    		}

	    		this.repaint();
	    		Thread.sleep(1000); 
	    	}
		} catch (Exception e)
		{ 
			e.printStackTrace();
		}	
	}
	
	public String getPlayerName() 
	{
		return _name;
	}

	public String getHrMinSec(long millisec) 
	{
		String ms = String.format("%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millisec),
				TimeUnit.MILLISECONDS.toMinutes(millisec)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisec)),
				TimeUnit.MILLISECONDS.toSeconds(millisec)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisec)));
		return ms;
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

	public Server getPlayerServer() 
	{
		return _server;
	}

	public Race getPlayerRace() 
	{
		return _race;
	}

	public Rank getPlayerRank() 
	{
		return _rank;
	}

	
}

package areas;

import java.awt.Color;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Times;

public class AbilityBar extends JPanel
{
	String abilityName;
	Date startTime;
	private JLabel label;
	PlayerAbilityWatcher parent;
	
	public AbilityBar(PlayerAbilityWatcher parent, String abilityName, Date startTime)
	{
		this.parent = parent;
		this.abilityName = abilityName;
		this.startTime = startTime;
		Date d = new Date();
		long diff = d.getTime() - startTime.getTime();
		label = new JLabel(abilityName + " - " + diff);
		this.setAlignmentX(LEFT_ALIGNMENT);
		add(label);
	
		
		 Runnable r = new Runnable() 
		 {
		      public void run() 
		      {
		        try 
		        {
		        	cooldownTick();
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
	
	protected void cooldownTick() 
	{
		try
		{
			while(true)
			{
	    		Date currentTime = new Date();
	    		long difference = currentTime.getTime() - startTime.getTime();
	    		long countDown = Times.ONE_MINUTE - difference;
	    		if(countDown<0)
	    		{
	    			countDown = 0;
	    		}
	    		
	    		double scale = ((double) countDown / (double) Times.ONE_MINUTE);
	    		double midLine = 150;
	    		int edge = (int) (255 - midLine);
	    		int gScale = (int) (midLine * scale);
	    		int rScale = (int) (midLine-(midLine * scale));
	    		
	    		int R = edge/2+rScale;
	    		int G = edge/2+gScale;
	    		int B = 0;
	    		
	    		if(R < 0 || R > 255) R=0;
	    		if(G < 0 || G > 255) G=0;
	    		if(B < 0 || B > 255) B=0;
	    		
	    		label.setForeground(new Color(R, G, B));
				label.setText(abilityName + " - " + Times.getMinSec(countDown)); 
				
				if(countDown <= 0)
				{
					removeThisBar();
					return;
				}
				Thread.sleep(1000);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void removeThisBar() 
	{
		parent.remove(this);
		parent.revalidate();
		parent.repaint();
	}

	public void setText(String txt)
	{
		label.setText(txt);
	}
}







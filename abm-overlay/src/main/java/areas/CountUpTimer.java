package areas;

import java.awt.Color;
import java.awt.event.ActionListener;

import utils.Times;

public class CountUpTimer extends BaseTimer implements ActionListener
{
	public CountUpTimer(String infoTxt)
	{
		super("00:00", infoTxt);
	}

	@Override
	protected void timer() 
	{
		try
		{
			while(true)
			{
				if(!isStopped())
				{
					//Countup
					long diff = System.currentTimeMillis() - _startTime;
					String milliSec = diff + "";
					if(milliSec.length() >= 3)
					{
						milliSec = milliSec.substring(milliSec.length() - 3, milliSec.length() - 1);
					}
					
					
					Color c = getColorBasedOnTime(diff);				
					String time = Times.getMinSec(diff);
					
					playSoundIfNeeded(time);
					
				
					_ms.setForeground(c);
					_milli.setForeground(c);
					
					_ms.setText(time);
					_milli.setText(milliSec);		
				}
				else
				{
					System.out.println("Killing this timer");
					return;
				}
				Thread.sleep(10);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected Color getColorBasedOnTime(long diff) 
	{
		//01:00 - notification at 00:55		FIRST_WAVE
		//03:30 - notification at 03:25		SECOND_WAVE
		//06:00 - notification at 05:55		THIRD_WAVE
		
		double scaledTo;
		double percentLeft;
		
		if(diff<= FIRST_WAVE)
		{
			scaledTo = FIRST_WAVE;
			percentLeft = FIRST_WAVE - diff;
		}
		else if (diff <= SECOND_WAVE)
		{
			scaledTo = SECOND_WAVE;
			percentLeft = SECOND_WAVE - diff + FIRST_WAVE;
		}
		else if (diff <= THIRD_WAVE)
		{
			scaledTo = THIRD_WAVE;
			percentLeft = THIRD_WAVE - diff +  SECOND_WAVE;
		}
		else
		{
			scaledTo = 1000000000;
			percentLeft = 1000000000 - diff + THIRD_WAVE;

		}
		
		double scale = ((double) percentLeft / (double) scaledTo);
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
		
		return new Color(R, G, B);
	}	
		
}

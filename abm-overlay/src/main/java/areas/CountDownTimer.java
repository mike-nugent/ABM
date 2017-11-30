package areas;

import java.awt.Color;
import java.awt.event.ActionListener;

import utils.Times;

public class CountDownTimer extends BaseTimer implements ActionListener
{
	long _countDownFrom;
	public CountDownTimer(long countDown, String infoTxt)
	{ 
		super(Times.getMinSec(countDown), infoTxt);
		_countDownFrom = countDown;
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
					long countdown = _countDownFrom - diff;

					if(countdown <= 0)
					{
						countdown = 0;
					}
					String milliSec = countdown + "";

					
					if(milliSec.length() >= 3)
					{
						milliSec = milliSec.substring(milliSec.length() - 3, milliSec.length() - 1);
					}
					else
					{
						milliSec = "0";
					}
					
					
					Color c = getColorBasedOnTime(countdown);				
					String time = Times.getMinSec(countdown);
					
					playSoundIfNeeded(time);
					
				
					_ms.setForeground(c);
					_milli.setForeground(c);
					
					_ms.setText(time);
					_milli.setText(milliSec);		
					
					if(countdown == 0)
					{
						return;
					}
					
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

		double scale = ((double) diff / (double) _countDownFrom);
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

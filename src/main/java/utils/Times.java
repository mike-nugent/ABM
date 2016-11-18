package utils;

import java.util.concurrent.TimeUnit;

public class Times 
{
	public static final long THIRTY_FIVE_MINUTES = 2100000;
	public static final long FIFTEEN_MINUTES = 900000;
	public static final long TEN_MIUTES = 600000;
	public static final long ONE_MINUTE = 60000;
	public static final long ONE_HOUR = 6 * TEN_MIUTES;
	public static final long FIVE_MINUTES = 300000;
	
	 
	public static String getHrMinSec(long millisec) 
	{
		String ms = String.format("%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millisec),
				TimeUnit.MILLISECONDS.toMinutes(millisec)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisec)),
				TimeUnit.MILLISECONDS.toSeconds(millisec)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisec)));
		return ms;
	}
	
	public static String getMinSec(long millisec) 
	{
		String ms = String.format("%02d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(millisec)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisec)),
				TimeUnit.MILLISECONDS.toSeconds(millisec)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisec)));
		return ms;
	}
}
 
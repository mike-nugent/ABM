package handlers;

import java.sql.Time;

public class TimeManager 
{

	public static void startTime(String timeData) 
	{
		String mode = timeData.substring(0, timeData.indexOf(":"));
		String time = timeData.substring(timeData.indexOf(":") +1);
		Time t = Time.valueOf("00:"+time);

		System.out.println(mode + " " + time + " " +( t.getTime() - System.currentTimeMillis()));
		
	}

}

package gameinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public enum Server 
{
	Beritra 	("BR"),
	Israphel 	("IS"),
	Siel		("SL"),	
	Tiamat		("TM"),
	Kahrun		("KR"),
	Unknown		("??");
	
	private String _serverString;
	private Server(String serverString)
	{
		_serverString = serverString;
	}
	
	public String getServerString()
	{
		 return  _serverString;
	}
	
	public static Server getServer(String serverString)
	{
		if(serverString == null)
			return Server.Unknown;
		
		for(Server s : values())
		{
			if(s.getServerString().equals(serverString))
			{
				return s;
			}
		}
		
		try
		{
			Server s = Server.valueOf(serverString);
			return s;
		} 
		catch (Exception e)
		{
			return Unknown;
		}
	}

	public static Server getRandom() 
	{
		Random random = new Random();
		return Server.values()[(random.nextInt(values().length -1))];
	}

	public static Vector<Server> getLegalValues() 
	{
		Vector<Server> returnLst = new Vector<Server>();
		for(Server s : values())
		{
			if(!s.equals(Unknown))
			{
				returnLst.add(s);
			}
		}
		
		return returnLst;
	}
}

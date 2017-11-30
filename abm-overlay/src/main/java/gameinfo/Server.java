package gameinfo;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public enum Server
{
    Katalam("KT", "Katal"), Danaria("DN", "Danar"), Beritra("BR"), Israphel("IS"), Siel("SL"), Tiamat("TM"), Kahrun("KR"), Unknown("??");

    private String[] _serverStrings;

    private Server(final String... serverString)
    {
        _serverStrings = serverString;
    }

    public String getServerString()
    {
        return _serverStrings[0];
    }

    public List<String> getServerStrings()
    {
        return Arrays.asList(_serverStrings);
    }

    public static Server getServer(final String serverString)
    {
        if (serverString == null)
        {
            return Server.Unknown;
        }

        for (final Server s : values())
        {
            if (s.getServerStrings().contains(serverString))
            {
                return s;
            }
        }

        try
        {
            final Server s = Server.valueOf(serverString);
            return s;
        }
        catch (final Exception e)
        {
            return Unknown;
        }
    }

    public static Server getRandom()
    {
        final Random random = new Random();
        return Server.values()[(random.nextInt(values().length - 1))];
    }

    public static Vector<Server> getLegalValues()
    {
        final Vector<Server> returnLst = new Vector<Server>();
        for (final Server s : values())
        {
            if (!s.equals(Unknown))
            {
                returnLst.add(s);
            }
        }

        return returnLst;
    }
}

package gameinfo;

import java.util.Random;

public enum HackList
{
    Suspected("Suspected"),
    Confirmed("Confirmed"), 
    No_Accusations("No Accusations");

    private String _title;

    private HackList(final String title)
    {
        _title = title;
    }

    public String getTitle()
    {
        return _title;
    }

    public static HackList getRandom()
    {
        final Random random = new Random();
        return HackList.values()[(random.nextInt(values().length))];
    }
}

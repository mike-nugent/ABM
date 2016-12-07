package database;

import java.util.List;

import gameinfo.PlayerData;

public class PlayerBaseUpdater
{
    private static List<PlayerData> allPlayers;

    public static void initialize()
    {
        allPlayers = AionDB.getAllPlayers();
    }

    public static void addOrUpdatePlayer(final PlayerData data)
    {
        final String name = data.name;

        if (name != null)
        {
            for (final PlayerData storedPlayer : allPlayers)
            {
                if (storedPlayer.getName().equals(name))
                {
                    // Player found. Check to see if needs update;
                    // Do a compare on fields, update the DB if new
                    return;
                }
            }

            // No player was found matching this data. Add it.
            AionDB.addOrUpdatePlayer(data);
            allPlayers.add(data);
        }
    }
}

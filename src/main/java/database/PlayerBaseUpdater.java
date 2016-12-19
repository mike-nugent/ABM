package database;

import java.util.List;

import gameinfo.AbilityData;
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
            AionDB.addOrUpdatePlayer(data.name, data.server, data.race, data.clazz, data.rank);
            allPlayers.add(data);
        }
    }

    public static void addOrUpdatePlayer(final AbilityData data)
    {
        final boolean casterExists = checkName(data.caster);
        final boolean targetExists = checkName(data.target);

        if (!casterExists)
        {
            // No player was found matching this data. Add it.
            if (data.caster != null && !data.caster.contains(" "))
            {
                AionDB.addOrUpdatePlayer(data.caster, data.casterServer, null, null, null);
                allPlayers.add(new PlayerData(null, data.caster, data.casterServer, null, null, null, null));
            }
        }

        if (!targetExists && !data.target.equals(data.caster))
        {
            // Enemy names commonly have spaces, ignore those.
            if (data.target != null && !data.target.contains(" "))
            {
                AionDB.addOrUpdatePlayer(data.target, data.targetServer, null, null, null);
                allPlayers.add(new PlayerData(null, data.target, data.targetServer, null, null, null, null));
            }
        }
    }

    private static boolean checkName(final String name)
    {
        for (final PlayerData storedPlayer : allPlayers)
        {
            if (storedPlayer.getName().equals(name))
            {
                return true;
            }
        }

        return false;
    }
}

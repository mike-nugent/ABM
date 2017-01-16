package database;

import java.util.List;

import abilities.Ability;
import gameinfo.AbilityData;
import gameinfo.Archetype;
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
        final PlayerData caster = checkName(data.caster);
        final PlayerData target = checkName(data.target);

        if (caster == null || Archetype.Unknown.equals(caster.clazz))
        {
            // No player was found matching this data. Add it.
            if (data.caster != null && !data.caster.contains(" "))
            {
                final Archetype clazz = Ability.locateArchetype(data.ability);
                AionDB.addOrUpdatePlayer(data.caster, data.casterServer, null, clazz, null);
                if (caster == null)
                {
                    allPlayers.add(new PlayerData(null, data.caster, data.casterServer, null, null, clazz, null));
                }
                else
                {
                    caster.setClazz(clazz);
                }
            }
        }

        if (target == null && !data.target.equals(data.caster))
        {
            // Enemy names commonly have spaces, ignore those.
            if (data.target != null && !data.target.contains(" "))
            {
                AionDB.addOrUpdatePlayer(data.target, data.targetServer, null, null, null);
                allPlayers.add(new PlayerData(null, data.target, data.targetServer, null, null, null, null));
            }
        }
    }

    private static PlayerData checkName(final String name)
    {
        for (final PlayerData storedPlayer : allPlayers)
        {
            if (storedPlayer.getName().equals(name))
            {
                return storedPlayer;
            }
        }

        return null;
    }
}

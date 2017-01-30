package database;

import java.util.List;

import abilities.Ability;
import gameinfo.AbilityData;
import gameinfo.Archetype;
import gameinfo.PlayerData;
import gameinfo.Rank;
import gameinfo.Server;

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
        final Server server = data.server;
        final boolean nameIsValid = name != null && !name.trim().equals("") && !name.trim().contains(" ");
        final boolean serverIsValid = server != null && !server.equals(Server.Unknown);

        if (nameIsValid && serverIsValid)
        {
            final PlayerData player = findPlayerByNameAndServer(name, server);
            if (player == null)
            {
                // No player was found matching this data. Add it.
                AionDB.addNewPlayer(name, server, data.race, data.clazz, data.rank);
                allPlayers.add(data);
            }
            else
            {
            	//only update if the new information is better than old.
            	Rank rank = player.rank;
            	if(rank != null && !rank.equals(Rank.Unknown))
            	{
            		if(!rank.equals(data.rank))
            		{
                        AionDB.updateExistingPlayer(name, server, player.race, player.clazz, data.rank);
                        player.setRank(data.rank);
            		}
            	}
            }
        }
    }

    public static void addOrUpdatePlayer(final AbilityData data)
    {
        final String name = data.caster;
        final Server server = data.casterServer;

        final boolean nameIsValid = name != null && !name.trim().equals("") && !name.trim().contains(" ");

        if (nameIsValid)
        {
            final PlayerData existingCaster = findPlayerByNameAndServer(name, server);
            // final PlayerData existingTarget =
            // findPlayerByNameAndServer(data.target, data.targetServer);
            final Archetype clazz = Ability.locateArchetype(data.ability);

            if (existingCaster != null)
            {
                if (clazz != null && !clazz.equals(Archetype.Unknown))
                {
                    if (!existingCaster.clazz.equals(clazz))
                    {
                        AionDB.updateExistingPlayer(name, server, existingCaster.race, clazz, existingCaster.rank);
                        existingCaster.setClazz(clazz);
                    }
                }
            }
            else
            {
                AionDB.addNewPlayer(name, server, null, clazz, null);
                allPlayers.add(new PlayerData(null, name, server, null, null, clazz, null));
            }
        }
    }

    private static PlayerData findPlayerByNameAndServer(final String name, final Server server)
    {
        for (final PlayerData storedPlayer : allPlayers)
        {
            if (storedPlayer.getName().equals(name) && storedPlayer.server.equals(server))
            {
                return storedPlayer;
            }
        }

        return null;
    }
}

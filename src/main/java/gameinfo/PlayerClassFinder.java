package gameinfo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import xforms.XformLoader;

public class PlayerClassFinder
{
    // Map contains a list of server-race to a map of the players. for instance:
    // SL-E -> Map<name, csv string>

    static Map<String, Map<String, String>> allPlayers = new HashMap<String, Map<String, String>>();

    public static String findClass(final String name, final Server server, final Race race)
    {
        final Map<String, String> playerList = getPlayerMap(server, race);

        final String csvLine = playerList.get(name);
        if (csvLine != null)
        {
            final String[] csvSplit = csvLine.split(",");
            if (csvSplit.length == 2)
            {
                final String clazz = csvSplit[1];
                return clazz;
            }
        }

        return Archetype.Unknown.name();
    }

    private static Map<String, String> loadPlayerList(final Server server, final Race race)
    {

        final Map<String, String> returnMap = new HashMap<String, String>();
        final String playerFile = server.name() + "-" + race.name() + "-Transforms.csv";
        System.out.println("Loading file: " + playerFile);
        Scanner sc = null;
        InputStream in = null;
        try
        {
            in = XformLoader.class.getResourceAsStream(playerFile);
            sc = new Scanner(in, "UTF-8");
            while (sc.hasNextLine())
            {
                final String line = sc.nextLine();
                if (line != null)
                {
                    final String[] split = line.split(",");
                    if (split.length > 0)
                    {
                        final String name = split[0];
                        returnMap.put(name, line);
                    }
                }
            }
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (final IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (sc != null)
            {
                sc.close();
            }
        }

        return returnMap;
    }

    public static void updatePlayer(final PlayerData data)
    {
        final Map<String, String> playerList = getPlayerMap(data.server, data.race);
        if (!playerList.containsKey(data.name))
        {
            // System.out.println("Adding player: \n" + data.name);
            // System.out.println(data.race);
            // System.out.println(data.server);
            // System.out.println(data.location);
            // System.out.println(data.rank);
            // System.out.println("\n");
            playerList.put(data.name, data.name + ",?");
        }
    }

    public static void flushDataToFiles()
    {
        for (final Server s : Server.values())
        {
            if (!s.equals(Server.Unknown))
            {
                System.out.println(s);
                final Map<String, String> asmoList = getPlayerMap(s, Race.Asmodian);
                final Map<String, String> elyList = getPlayerMap(s, Race.Elyos);

                flushToFile(asmoList, s, Race.Asmodian);
                flushToFile(elyList, s, Race.Elyos);
            }
        }

    }

    private static void flushToFile(final Map<String, String> list, final Server s, final Race r)
    {
        final String playerFile = "src/main/resources/xforms/" + s.name() + "-" + r.name() + "-Transforms.csv";

        FileWriter fstream = null;
        try
        {
            fstream = new FileWriter(playerFile);
        }
        catch (final IOException e1)
        {
            e1.printStackTrace();
        }
        final BufferedWriter out = new BufferedWriter(fstream);

        for (final String line : list.values())
        {
            try
            {
                out.write(line + "\n");
                out.flush();
            }
            catch (final IOException e)
            {
                e.printStackTrace();
            }
        }

        if (out != null)
        {
            try
            {
                out.close();
            }
            catch (final IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void updatePlayerByAbility(final AbilityData data)
    {
        if (data == null)
        {
            return;
        }

        final List<Map<String, String>> playerLists = getPlayerMaps(data.casterServer);
        for (final Map<String, String> m : playerLists)
        {
            // only one of the maps will, most likely neither will.
            if (m.containsKey(data.caster))
            {
                String line = m.get(data.caster);
                if (line.contains(",?") || line.contains(",Unknown"))
                {
                    line = line.replace(",?", "");
                    line = line.replace(",Unknown", "");

                    final Archetype clazz = Ability.locateArchetype(data.ability);
                    if (!clazz.equals(Archetype.Unknown))
                    {
                        line = line + "," + clazz;
                        m.put(data.caster, line);
                        System.out.println("Updating :" + line);
                    }
                }
            }
        }
    }

    private static Map<String, String> getPlayerMap(final Server server, final Race race)
    {
        final String key = server.getServerString() + "_" + race.getAcyonym();
        Map<String, String> playerList = allPlayers.get(key);
        if (playerList == null)
        {
            playerList = loadPlayerList(server, race);
            allPlayers.put(key, playerList);
        }
        return playerList;
    }

    private static List<Map<String, String>> getPlayerMaps(final Server server)
    {
        final List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
        ret.add(getPlayerMap(server, Race.Asmodian));
        ret.add(getPlayerMap(server, Race.Elyos));
        return ret;
    }

}

package gameinfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import config.ConfigFile;
import utils.RandomNameGenerator;

public class PlayerData
{
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public String    name;
    public Server    server;
    public Rank      rank;
    public Race      race;
    public Archetype clazz;
    public Date      dateTime; // The time the log took place
    public String    location; // Information about the zone, if
                               // available
    public HackList  hacks;    // hacking accusations

    public PlayerData(final Date dateTime, final String name, final Server server, final Race race, final Rank rank,
            final Archetype clazz, final String location)
    {
        this.dateTime = dateTime;
        this.name = name;
        this.server = server;
        this.rank = rank;
        this.race = race;
        this.clazz = clazz;
        this.location = location;
        this.hacks = HackList.No_Accusations;
    }

    public static PlayerData fromDB(final Date dateTime, final String name, final String server, final String race,
            final String rank, final String archtype, final String location)
    {
        return new PlayerData(new Date(), name, Server.getServer(server), Race.getRace(race), Rank.getRank(rank),
                Archetype.fromString(archtype), location);

    }

    public static PlayerData parseXform(final String xformLine)
    {
        final String[] parsed = xformLine.split(" ");
        String date = parsed[0];
        String time = parsed[1];
        String name = parsed[3];
        String server = name.contains("-") ? name.substring(name.indexOf("-") + 1)
                : ConfigFile.getServer().getServerString();
        String race = xformLine.contains("Asmodian") ? "Asmodian" : "Elyos";
        String rank = xformLine.substring(xformLine.indexOf("Guardian General ") + 16, xformLine.lastIndexOf(" in "));
        String location = xformLine.substring(xformLine.lastIndexOf(" in ") + 4, xformLine.lastIndexOf("."));

        if (name.trim().equals(ConfigFile.getName()))
        {
            name = ConfigFile.getName();
        }

        if (name.contains("-"))
        {
            name = name.substring(0, name.indexOf("-"));
        }

        date = date.trim();
        time = time.trim();
        name = name.trim();
        server = server.trim();
        race = race.trim();
        rank = rank.trim();
        location = location.trim();

        Date DT = null;
        try
        {
            DT = formatter.parse(date + " " + time);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        final String N = name;
        final Server S = Server.getServer(server);
        final Race RA = Race.getRace(race);
        final Rank R = Rank.getRank(rank);

        // Has to be looked up by name
        final Archetype C = Archetype.getArchetype(name, S, RA);
        final String L = location;

        final PlayerData data = new PlayerData(DT, N, S, RA, R, C, L);

        return data;
    }

    private static String fixDeathLineForParsing(final String line)
    {
        if (line.contains("Officer"))
        {
            return line.replace("Officer", "START:");
        }
        else if (line.contains("General"))
        {
            return line.replace("General", "START:");
        }
        else if (line.contains("Commander"))
        {
            return line.replace("Commander", "START:");
        }
        else if (line.contains("Governor"))
        {
            return line.replace("Governor", "START:");
        }

        System.out.println("ERROR - could not transform line: " + line);
        return line;
    }

    public static Date getDateTime(final String line)
    {
        final String[] parsed = line.split(" ");
        final String date = parsed[0];
        final String time = parsed[1];
        final String dateTime = (date.trim() + " " + time.trim()).trim();
        try
        {
            return formatter.parse(dateTime);
        }
        catch (final ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static PlayerData parseDeath(final String line)
    {
        final String reWorkedLine = fixDeathLineForParsing(line);
        String name = reWorkedLine.substring(reWorkedLine.indexOf("START:") + 6, reWorkedLine.indexOf(" has died in "));
        final String server = name.contains("-") ? name.substring(name.indexOf("-") + 1)
                : ConfigFile.getServer().getServerString();
        final String race = line.contains("Asmodian") ? "Asmodian" : "Elyos";
        final String location = line.substring(line.lastIndexOf(" in ") + 4, line.lastIndexOf("."));

        if (name.contains("-"))
        {
            name = name.substring(0, name.indexOf("-")).trim();
        }

        if (name.trim().equals(ConfigFile.getName()))
        {
            name = ConfigFile.getName();
        }

        final String rank = line.substring(line.lastIndexOf(" Army ") + 6, line.indexOf(name)).trim();

        final Date DT = getDateTime(line);
        final String N = name.trim();
        final Server S = Server.getServer(server);
        final Race R = Race.getRace(race);
        final Rank RA = Rank.getRank(rank);
        final Archetype C = Archetype.getArchetype(name, S, R);
        final String L = location.trim();

        return new PlayerData(DT, N, S, R, RA, C, L);
    }

    public static PlayerData generateRandom()
    {
        final Date dateTime = new Date();
        final String name = RandomNameGenerator.generateName();
        final Server server = Server.getRandom();
        final Race race = Race.getRandom();
        final Rank rank = Rank.getRandom();
        final Archetype clazz = Archetype.getRandom();
        final String location = "Yo mama";

        final PlayerData p = new PlayerData(dateTime, name, server, race, rank, clazz, location);
        p.hacks = HackList.getRandom();
        return p;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getServer()
    {
        return server.name();
    }

    public void setServer(final Server server)
    {
        this.server = server;
    }

    public String getRank()
    {
        return rank.getRankTitle();
    }

    public void setRank(final Rank rank)
    {
        this.rank = rank;
    }

    public String getRace()
    {
        return race.name();
    }

    public void setRace(final Race race)
    {
        this.race = race;
    }

    public String getClazz()
    {
        return clazz.name();
    }

    public void setClazz(final Archetype clazz)
    {
        this.clazz = clazz;
    }

    public String getDateTime()
    {
        return dateTime.toString();
    }

    public void setDateTime(final Date dateTime)
    {
        this.dateTime = dateTime;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(final String location)
    {
        this.location = location;
    }

    public String getHacks()
    {
        return hacks.getTitle();
    }

    public void setHacks(final HackList hacks)
    {
        this.hacks = hacks;
    }

}

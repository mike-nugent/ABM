package handlers;

import java.util.Date;

import config.ConfigFile;
import database.PlayerBaseUpdater;
import gameinfo.Archetype;
import gameinfo.PlayerData;
import gameinfo.Race;
import gameinfo.Rank;
import gameinfo.Server;
import javafx.application.Platform;
import main.DisplayManager;

public class DeathLineHandler extends LineHandler
{

    /**
     * (X + "has died in" + X)
     *
     * 2015.12.30 23:32:56 : Asmodian Asmodian Army 5-Star Officer LunyWitch has
     * died in Crimson Temple. 2015.12.30 23:32:59 : Elyos Elyos Army 5-Star
     * Officer Airixx has died in Vorgaltem Battlefield. 2015.12.30 23:32:59 :
     * Elyos Elyos Army 5-Star Officer Karaas has died in Crimson Temple.
     * 2015.12.30 23:33:04 : Elyos Elyos Army 5-Star Officer Thebigkanon has
     * died in Crimson Temple. 2015.12.30 23:33:11 : Asmodian Asmodian Army
     * Great General xHermionex has died in Crimson Temple. 2015.12.30 23:33:15
     * : Elyos Elyos Army General Insocturno has died in Crimson Temple.
     * 2015.12.30 23:33:21 : Elyos Elyos Army Great General MelanieMamel has
     * died in Crimson Temple.
     */
    public DeathLineHandler()
    {
        super(ranked_death);

    }

    public PlayerData parseDeathLine(final String line)
    {
        try
        {
            final String reWorkedLine = fixDeathLineForParsing(line);
            String name = reWorkedLine.substring(reWorkedLine.indexOf("START:") + 6,
                    reWorkedLine.indexOf(" has died in "));
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

            final PlayerData data = new PlayerData(DT, N, S, R, RA, C, L);
            return data;
        }
        catch (final Exception e)
        {
            showError(e, line);
            return null;
        }
    }

    @Override
    protected void handleLine(final String line, boolean isCurrent)
    {
        final PlayerData data = parseDeathLine(line);

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("sending data now...");
                DisplayManager.checkPlayerDeath(data);
                PlayerBaseUpdater.addOrUpdatePlayer(data);
            }
        });
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

}

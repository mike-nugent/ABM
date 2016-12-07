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
import main.TransformManager;

public class TransformLineHandler extends LineHandler
{

    /**
     * (X + "of" + X + "uses Transformation: Guardian General" + X + "in" + X)
     *
     * 2015.12.11 23:10:02 : WalkOn of Asmodian uses Transformation: Guardian
     * General I in Vorgaltem Battlefield. 2015.12.11 23:16:44 : Noariza of
     * Elyos uses Transformation: Guardian General I in Vorgaltem Citadel.
     * 2015.12.11 23:16:45 : IITakyzOII of Asmodian uses Transformation:
     * Guardian General I in Vorgaltem Citadel. 2015.12.11 23:16:46 :
     * TheDeathGiver of Asmodian uses Transformation: Guardian General II in
     * Vorgaltem Citadel. 2015.12.11 23:16:49 : Aioroz of Asmodian uses
     * Transformation: Guardian General V in Vorgaltem Citadel.
     */
    public TransformLineHandler()
    {
        super(transform);
    }

    public PlayerData parseTransformLine(final String line)
    {
        try
        {
            final String[] parsed = line.split(" ");
            String date = parsed[0];
            String time = parsed[1];
            String name = parsed[3];
            String server = name.contains("-") ? name.substring(name.indexOf("-") + 1)
                    : ConfigFile.getServer().getServerString();
            String race = line.contains("Asmodian") ? "Asmodian" : "Elyos";
            String rank = line.substring(line.indexOf("Guardian General ") + 16, line.lastIndexOf(" in "));
            String location = line.substring(line.lastIndexOf(" in ") + 4, line.lastIndexOf("."));

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
        catch (final Exception e)
        {
            showError(e, line);
            return null;
        }
    }

    @Override
    protected void handleLine(final String line)
    {
        final PlayerData data = parseTransformLine(line);

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("sending data now...");
                TransformManager.transformDetected(data);
                PlayerBaseUpdater.addOrUpdatePlayer(data);
            }
        });
    }
}

package handlers;

import java.util.Date;

import config.ConfigFile;
import database.PlayerBaseUpdater;
import gameinfo.AbilityData;
import gameinfo.Server;

public class UsesCooldownLineHandler extends LineHandler
{

    /**
     *
     * <pre>
      (X + "is in the" + X + "because" + X + "used" + X)

       2015.12.04 00:44:56 : NoiseCntrl is in the boost Stumble Resist,Silence Resistance,Knock Back Resist state because NoiseCntrl used Mvt. 3: Autumn.
       2015.12.04 00:44:59 : NoiseCntrl is in the boost PvP Defense state because NoiseCntrl used Protective Ode.
       2015.12.04 00:54:53 : Judasiscariote is in the boost PvP Defense state because Judasiscariote used Stone Skin.
       2015.12.04 00:55:11 : MasterG is in the boost PvP Defense state because MasterG used Stone Skin.
       2015.12.04 01:04:45 : Earth Spirit is in the boost Magical Acc,Magic Boost,Magic Suppression state because Judasiscariote used Sympathetic Mind.
       2015.12.04 01:07:42 : Dahacka is in the boost Natural Healing,Natural Mana Treatment,HP state because Dahacka used Breath of Nature.
       2015.12.04 01:08:43 : Temudjin is in the boost PvP Defense state because Temudjin used Stone Skin.
       2015.12.04 01:11:09 : anmarie is in the boost Natural Healing,Natural Mana Treatment state because anmarie used Nature's Favor.
       2015.12.04 00:06:01 : Sasorri is in the boost Stun, Knock Back, Stumble, Spin, and Aerial Thrust Resistance Values state because Sasorri used Remove Shock I.
     *
     * </pre>
     */
    public UsesCooldownLineHandler()
    {
        super(buff);
        ignoreAtStartup = true;
    }

    public AbilityData parseCooldownLine(final String line)
    {
        try
        {
            String trimmed = line.substring(line.indexOf(" : ") + 3);
            trimmed = trimmed.replace("Critical Hit!", "");
            String target = trimmed.substring(0, trimmed.indexOf(" is in the "));
            String caster = trimmed.substring(trimmed.indexOf(" because ") + 9, trimmed.lastIndexOf(" used "));
            final String ability = trimmed.substring(trimmed.lastIndexOf(" used ") + 6, trimmed.lastIndexOf("."));

            final String casterServer = caster.contains("-") ? caster.substring(caster.indexOf("-") + 1)
                    : ConfigFile.getServer().getServerString();
            final String targetServer = target.contains("-") ? target.substring(target.indexOf("-") + 1)
                    : ConfigFile.getServer().getServerString();

            if (target.contains("-"))
            {
                target = target.substring(0, target.indexOf("-"));
            }

            if (caster.contains("-"))
            {
                caster = caster.substring(0, caster.indexOf("-"));
            }

            final Date D = getDateTime(line);
            final String T = target.trim();
            final String C = caster.trim();
            final String A = ability.trim();
            final Server TS = Server.getServer(targetServer.trim());
            final Server CS = Server.getServer(casterServer.trim());

            final AbilityData data = new AbilityData(D, T, C, A, TS, CS);
            return data;
        }
        catch (final Exception e)
        {
            showError(e, line);
            return null;
        }
    }

    @Override
    protected void handleLine(final String line, final boolean isCurrent)
    {
        final AbilityData data = parseCooldownLine(line);
        if (data != null)
        {
            PlayerBaseUpdater.addOrUpdatePlayer(data);
        }
    }
}

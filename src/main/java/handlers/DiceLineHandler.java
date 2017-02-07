package handlers;

import java.util.regex.Pattern;

import config.ConfigFile;

public class DiceLineHandler extends LineHandler
{
    public DiceLineHandler()
    {
        super(gave_up, rolled_dice, rolled_highest, everyone_pass, has_acquired);
    }

    @Override
    protected void handleLine(final Pattern pattern, final String line, final boolean isCurrent)
    {
        final String trimmed = line.substring(line.indexOf(" : ") + 3);

        if (pattern.equals(gave_up))
        {
            // 2016.12.09 23:48:11 : You gave up rolling the dice.
            // 2016.12.09 23:48:11 : Cydonia gave up rolling the dice.
            // 2016.12.09 23:48:11 : Moist gave up rolling the dice.

            final String person = trimmed.substring(0, trimmed.indexOf(" gave up"));

        }
        else if (pattern.equals(rolled_dice))
        {
            // 2016.12.19 23:42:45 : Allessandra-KR rolled the dice and got 752 (max. 1,000).
            // 2016.12.19 23:42:29 : You rolled the dice and got 955 (max. 1,000).
            final String person = trimmed.substring(0, trimmed.indexOf(" rolled the"));
            final String roll = trimmed.substring(trimmed.indexOf("and got ") + 8, trimmed.indexOf(" ("));
            final String limit = trimmed.substring(trimmed.indexOf("max. ") + 5, trimmed.indexOf(")"));

        }
        else if (pattern.equals(rolled_highest))
        {
            // 2016.12.11 00:15:38 : You rolled the highest.
            // 2016.12.10 23:55:30 : Coy rolled the highest (Coy rolled 88, while you passed).
            // 2016.12.10 23:40:01 : Nyinu rolled the highest (Nyinu rolled 90, while you passed).
            final String person = trimmed.substring(0, trimmed.indexOf(" rolled the"));
        }
        else if (pattern.equals(everyone_pass))
        {
            // 2016.12.10 23:58:21 : Everyone passed on rolling the dice.
        }
        else if (pattern.equals(has_acquired))
        {
            // 2016.12.09 22:00:45 : TakyzO has acquired [item:188052607].
            // 2016.12.09 22:00:38 : EmillieAutumn has acquired [item:188052607].
            // 2016.12.09 22:00:41 : You have acquired [item:186000243;ver7;;;;] and stored it in your special cube.
            // 2016.12.09 22:00:41 : You have acquired 5 [item:186000236;ver7;;;;]s and stored them in your special cube.
            // 2016.12.09 22:00:43 : Shokhan has acquired [item:188054375].
            // 2016.12.09 22:16:22 : You have acquired [item:188100291;ver7;;;;].
            // 2016.12.09 22:16:24 : You have acquired 3 [item:188052496;ver7;;;;](s).
            // 2016.12.09 22:16:42 : You have acquired 30 [item:166100011;ver7;;;;](s).

            String person = "";
            if (trimmed.contains("You have"))
            {
                person = ConfigFile.getName();
            }
            else
            {
                person = trimmed.substring(0, trimmed.indexOf(" has acquired"));
            }

            // Get the number between acquired and [item, if there is one.
            String numItems = trimmed.substring(trimmed.indexOf("acquired") + 8, trimmed.indexOf("[item"));
            numItems = numItems.trim();
            final int totalNumItems = parseNumItems(numItems);

            // get the item ID
            // 9 = total number of digits in the ID
            final String itemID = trimmed.substring(trimmed.indexOf("item:") + 5, 9);

        }
    }

    private int parseNumItems(final String numItems)
    {
        if (numItems.length() == 0)
        {
            return 1;
        }

        try
        {
            return Integer.parseInt(numItems);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            return 1;
        }
    }

}

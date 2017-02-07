package handlers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LineHandler
{

    // Formatter for date/time
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    // regex for "any string or *"
    protected static String X = ".*?";

    // Damage dealt:
    protected static Pattern auto_attack    = Pattern.compile(X + "inflicted" + X + "damage on" + X);
    protected static Pattern ability_attack = Pattern.compile(X + "inflicted" + X + "damage on" + X + "by using" + X);
    protected static Pattern dot_attack     = Pattern.compile(X + "used" + X + "to inflict the continuous damage effect on" + X);

    // Damage received:
    protected static Pattern received_damage  = Pattern.compile(X + "received" + X + "because" + X + "used" + X);
    protected static Pattern received_damage2 = Pattern.compile(X + "received" + X + "due to the effect of" + X);

    // Recovery:
    protected static Pattern recovery1 = Pattern.compile(X + "recovered" + X + "due to the effects of" + X);
    protected static Pattern recovery2 = Pattern.compile(X + "recovered" + X + "by using" + X);
    protected static Pattern buff      = Pattern.compile(X + "is in the" + X + "because" + X + "used" + X);
    protected static Pattern strip     = Pattern.compile(X + "removed" + X + "by using" + X);
    protected static Pattern strip2    = Pattern.compile(X + "used" + X + "to remove" + X);

    // Pet
    protected static Pattern pet_command = Pattern.compile(X + "caused" + X + "to use" + X + "by using" + X);

    // Summon
    protected static Pattern summon = Pattern.compile(X + "summoned" + X + "by using" + X);

    // Cancel Ability:
    protected static Pattern stop_ability = Pattern.compile(X + "stopped using " + X);

    // Transform:
    protected static Pattern transform = Pattern.compile(X + "of" + X + "uses Transformation: Guardian General" + X + "in" + X);

    // Death:
    protected static Pattern ranked_death = Pattern.compile(X + "has died in" + X);
    protected static Pattern pvp_death    = Pattern.compile(X + "was killed by" + X);

    // Artifacts:
    protected static Pattern capture_arti = Pattern.compile(X + "of" + X + "has captured the" + X + "Artifact" + X);
    protected static Pattern lose_arti    = Pattern.compile(X + "Artifact has been lost to" + X);

    // Dice
    protected static Pattern gave_up        = Pattern.compile(X + "gave up rolling the dice" + X);
    protected static Pattern rolled_dice    = Pattern.compile(X + "rolled the dice and got" + X);
    protected static Pattern rolled_highest = Pattern.compile(X + "rolled the highest" + X);
    protected static Pattern everyone_pass  = Pattern.compile(X + "Everyone passed on rolling the dice." + X);
    protected static Pattern has_acquired   = Pattern.compile(X + "acquired *[item:" + X + "]" + X);

    protected Pattern[] _patterns;

    protected boolean ignoreAtStartup = false;

    static List<LineHandler> handlers = null;

    public LineHandler(final Pattern... patterns)
    {
        _patterns = patterns;
    }

    /**
     * Handles the line that matches the regular expression. Calls the appropriate method in the Main
     *
     * @param p
     *
     * @param b
     */
    public void handle(final Pattern pattern, final String line, final boolean isCurrent)
    {
        try
        {
            handleLine(pattern, line, isCurrent);
        }
        catch (final Exception e)
        {
            showError(e, line);
        }
    }

    /**
     * Must be overridden in sub-class and define implementation of handling the line
     *
     * @param pattern
     *
     * @param isCurrent
     *            TODO
     */
    protected abstract void handleLine(Pattern pattern, String line, boolean isCurrent);

    protected void showError(final Exception e, final String line)
    {
        System.out.println("Error in " + this + " could not parse line: " + line);
        e.printStackTrace();
    }

    /**
     * Checks to see if the line should be handled by this LineHandler.
     */
    public boolean handlesLine(final String line)
    {
        if (handlesLineGetPattern(line) != null)
        {
            return true;
        }

        return false;
    }

    public Pattern handlesLineGetPattern(final String line)
    {
        for (final Pattern pattern : _patterns)
        {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.matches())
            {
                return pattern;
            }
        }

        return null;
    }

    public static Date getDateTime(final String line)
    {
        try
        {
            if (line.contains(" : "))
            {
                final String[] parsed = line.split(" ");
                final String date = parsed[0];
                final String time = parsed[1];
                final String dateTime = (date.trim() + " " + time.trim()).trim();
                return formatter.parse(dateTime);
            }
            else
            {
                return null;
            }
        }
        catch (final Exception e)
        {
            // System.out.println("Splitting " + line);
            // e.printStackTrace();
            return null;
        }
    }

    /**
     * Add new line handlers here for them to be picked up / used by the program. Each handler is responsible for communicating with the Main class when it finds a line that matches its regular expression criteria.
     */
    public static List<LineHandler> getOrCreateHandlers()
    {
        if (handlers == null)
        {
            // The order of these should be added in order of most frequent ->
            // less frequent
            handlers = new ArrayList<LineHandler>();
            handlers.addAll(HandlerManager.getCustomHandlers());
            handlers.add(new InflictDamageLineHandler());
            handlers.add(new UsesCooldownLineHandler());
            handlers.add(new DeathLineHandler());
            handlers.add(new TransformLineHandler());
            handlers.add(new ArtifactCaptureLineHandler());
        }

        return handlers;
    }

    public static List<LineHandler> getLineHandlersForQuickHistory()
    {
        final List<LineHandler> tmp = getOrCreateHandlers();
        final List<LineHandler> ret = new LinkedList<LineHandler>();
        for (final LineHandler h : tmp)
        {
            if (!h.shouldBeIgnoredAtStartup())
            {
                ret.add(h);
            }
        }

        return ret;
    }

    private boolean shouldBeIgnoredAtStartup()
    {
        return ignoreAtStartup;
    }

    public static void addNewCustomHandler(final LineHandler handler)
    {
        handlers.add(handler);
    }

}

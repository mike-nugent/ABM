package gameinfo;

public class ScriptData
{
    private String    script;
    private final int id;

    private String line  = null;
    private String sound = null;
    private String alert = null;
    private String time  = null;

    public ScriptData(final int id, final String script)
    {
        this.script = script;
        this.id = id;

        parseData();
    }

    public void updateScript(final String newLine)
    {
        script = newLine;
        parseData();
    }

    public String getScript()
    {
        return script;
    }

    public int getID()
    {
        return id;
    }

    public String getLine()
    {
        return line;
    }

    public String getSound()
    {
        return sound;
    }

    public String getAlert()
    {
        return alert;
    }

    public String getTime()
    {
        return time;
    }

    private void parseData()
    {
        final String[] ops = script.split(",");
        for (final String s : ops)
        {
            if (s.contains("WHEN"))
            {
                line = s.replace("WHEN [", "");
                line = line.replace("]", "").trim();
            }
            else if (s.contains("PLAY"))
            {
                sound = s.replace("PLAY [", "");
                sound = sound.replace("]", "").trim();
            }
            else if (s.contains("SHOW"))
            {
                alert = s.replace("SHOW [", "");
                alert = alert.replace("]", "").trim();
            }
            else if (s.contains("START"))
            {
                time = s.replace("START [", "");
                time = time.replace("]", "").trim();
            }
        }
    }
}

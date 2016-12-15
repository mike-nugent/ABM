package handlers;

import java.util.regex.Pattern;

import gameinfo.ScriptData;

public class CustomLineHandler extends LineHandler
{
    private ScriptData data;

    public CustomLineHandler(final ScriptData data)
    {
        super(Pattern.compile(data.getLine().replaceAll("\\*", X)));

        this.data = data;

        System.out.println(_patterns[0]);
    }

    public void updateLineHandler(final ScriptData newData)
    {
        this.data = newData;

        System.out.println("Updating line handler.");
        System.out.println("incoming: " + newData.getLine());
        System.out.println("old: [" + _patterns[0] + "]");
        _patterns = new Pattern[]
        { Pattern.compile(newData.getLine().replaceAll("\\*", X)) };

        System.out.println("new: [" + _patterns[0] + "]");
    }

    @Override
    protected void handleLine(final String line)
    {
        System.out.println("Custom line handler captured!  Found line: " + line);
    }

    public int getID()
    {
        return data.getID();
    }
}

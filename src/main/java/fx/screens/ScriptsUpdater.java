package fx.screens;

import java.util.List;

import database.AionDB;
import gameinfo.ScriptData;

public class ScriptsUpdater
{
    private static List<ScriptData> allScripts;

    public static void initialize()
    {
        allScripts = AionDB.getAllScripts();
    }

    public static void deleteScript(final ScriptBarCreator bar)
    {
        ScriptsScreen.deleteScriptCreator(bar);
    }

    public static void createScript(final String completedScript, final ScriptBarCreator local)
    {
        final ScriptData returnData = AionDB.createScript(completedScript);
        ScriptsScreen.createScript(returnData, local);
        allScripts.add(returnData);
    }

    public static void populateScriptsFromDatabase()
    {
        // initialize from database
        for (final ScriptData script : allScripts)
        {
            System.out.println("Script: " + script.id + " " + script.script);
            ScriptsScreen.createScript(script, null);
        }
    }

    public static void updateScript(final ScriptData data)
    {
        AionDB.updateScript(data);
    }

    public static void deleteScript(final ScriptBar bar, final ScriptData data)
    {
        final boolean wasDeleted = ScriptsScreen.deleteScript(bar);
        if (wasDeleted)
        {
            AionDB.deleteScript(data);
            allScripts.remove(data);
        }
    }

}

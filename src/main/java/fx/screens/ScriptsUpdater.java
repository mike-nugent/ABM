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
        ScriptsPopupPage.deleteScriptCreator(bar);
    }

    public static void createScript(final String completedScript, final ScriptBarCreator local)
    {
        final ScriptData returnData = AionDB.createScript(completedScript);
        ScriptsPopupPage.createScript(returnData, local);
        allScripts.add(returnData);
    }

    public static void populateScriptsFromDatabase()
    {
        // initialize from database
        for (final ScriptData script : allScripts)
        {
            System.out.println("Script: " + script.id + " " + script.script);
            ScriptsPopupPage.createScript(script, null);
        }
    }

    public static void updateScript(final ScriptData data)
    {
        AionDB.updateScript(data);
    }

    public static void deleteScript(final ScriptBar bar, final ScriptData data)
    {
        AionDB.deleteScript(data);
        allScripts.remove(data);
        ScriptsPopupPage.deleteScript(bar);
    }

}

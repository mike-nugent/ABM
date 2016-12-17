package fx.screens;

import java.util.List;

import database.AionDB;
import gameinfo.ScriptData;
import handlers.HandlerManager;

public class ScriptsUpdater
{
    private static List<ScriptData> allScripts;

    public static void initialize()
    {
        allScripts = AionDB.getAllScripts();
    }

    public static List<ScriptData> getAllScripts()
    {
        return allScripts;
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
        HandlerManager.addNewHandler(returnData);
    }

    public static void populateScriptsFromDatabase()
    {
        // initialize from database
        for (final ScriptData script : allScripts)
        {
            System.out.println("Script: " + script.getID() + " " + script.getCompactedScript());
            ScriptsScreen.createScript(script, null);
        }
    }

    public static void updateScript(final ScriptData data)
    {
        System.out.println("before db: " + data.getCompactedScript());
        AionDB.updateScript(data);
        System.out.println("after db: " + data.getCompactedScript());

        HandlerManager.updateLineHandler(data);
    }

    public static void editScript(final ScriptBar bar, final boolean isEdit)
    {
        ScriptsScreen.editScript(bar, isEdit);
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

    public static void cancelEdit(final ScriptBar editBarRef, final int editRefIndex, final ScriptBarCreator editor)
    {
        ScriptsScreen.cancelEdit(editBarRef, editRefIndex, editor);
    }

}

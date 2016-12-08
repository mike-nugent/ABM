package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import config.ConfigFile;
import gameinfo.PlayerData;
import gameinfo.ScriptData;

public class AionDB
{
    private static Connection _conn;

    public static void instantiate()
    {
        try
        {
            Class.forName("org.h2.Driver");
            _conn = DriverManager.getConnection("jdbc:h2:file:" + ConfigFile.getOrCreateConfigDir() + "/aion-database",
                    "sa", "");
            System.out.println("Database connection established...");

            final Statement stmt = _conn.createStatement();
            // PLAYERS(NAME, SERVER, RACE, CLASS, RANK)
            // stmt.executeUpdate("DROP TABLE PLAYERS");
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS PLAYERS(NAME VARCHAR(255), SERVER VARCHAR(255), RACE VARCHAR(255), CLASS VARCHAR(255), RANK VARCHAR(255));");

            // SCRIPTS(LOG,TIME,SOUND,ALERT)
            // stmt.executeUpdate("DROP TABLE SCRIPTS");

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS SCRIPTS(ID INT NOT NULL AUTO_INCREMENT, SCRIPT VARCHAR(255) NOT NULL, PRIMARY KEY (ID));");

            // SOUNDS(NAME, PATH)

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SOUNDS(NAME VARCHAR(255), PATH VARCHAR(255));");
            final PlayerData r = PlayerData.generateRandom();
            // stmt.executeUpdate("INSERT INTO PLAYERS VALUES ( '" + r.name +
            // "', '" + r.server + "', '" + r.race + "', '"+ r.clazz + "', '" +
            // r.rank + "' )");

            /**
             * <pre>
             * final ResultSet rs = stmt.executeQuery("SELECT * FROM PLAYERS");
             * while (rs.next())
             * {
             *     final String name = rs.getString("NAME");
             *     final String server = rs.getString("SERVER");
             *     final String race = rs.getString("RACE");
             *     final String archtype = rs.getString("CLASS");
             *
             *     System.out.println(name + " " + server + " " + race + " " + archtype);
             * }
             * </pre>
             */
            stmt.close();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            System.out.println("Error in database");
        }
    }

    public static List<PlayerData> getAllPlayers()
    {
        try
        {
            final Statement stmt = _conn.createStatement();
            final List<PlayerData> returnData = new LinkedList<PlayerData>();
            final ResultSet rs = stmt.executeQuery("SELECT * FROM PLAYERS");
            while (rs.next())
            {
                final String name = rs.getString("NAME");
                final String server = rs.getString("SERVER");
                final String race = rs.getString("RACE");
                final String archtype = rs.getString("CLASS");
                final String rank = rs.getString("RANK");

                returnData.add(PlayerData.fromDB(new Date(), name, server, race, rank, archtype, "Bazaar"));
            }
            stmt.close();

            return returnData;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            System.out.println("Caught db error: " + e);
        }
        return null;
    }

    public static void teardown()
    {
        try
        {
            if (_conn != null)
            {
                _conn.close();
            }
        }
        catch (final Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<PlayerData> getPlayerByName(final String searchName)
    {
        try
        {
            final Statement stmt = _conn.createStatement();
            final List<PlayerData> returnData = new LinkedList<PlayerData>();
            final ResultSet rs = stmt
                    .executeQuery("SELECT * FROM PLAYERS WHERE upper(NAME) LIKE '%" + searchName.toUpperCase() + "%';");
            while (rs.next())
            {
                final String name = rs.getString("NAME");
                final String server = rs.getString("SERVER");
                final String race = rs.getString("RACE");
                final String archtype = rs.getString("CLASS");
                final String rank = rs.getString("RANK");

                returnData.add(PlayerData.fromDB(new Date(), name, server, race, rank, archtype, "Bazaar"));
            }
            stmt.close();

            return returnData;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            System.out.println("Caught db error: " + e);
        }
        return null;

    }

    public static void addOrUpdatePlayer(final PlayerData r)
    {

        try
        {
            final Statement stmt = _conn.createStatement();
            stmt.executeUpdate("INSERT INTO PLAYERS VALUES ( '" + r.name + "', '" + r.server + "', '" + r.race + "', '"
                    + r.clazz + "', '" + r.rank + "' )");

            stmt.close();

        }
        catch (final Exception e)
        {
            e.printStackTrace();
            System.out.println("Caught db error: " + e);
        }
    }

    public static List<ScriptData> getAllScripts()
    {
        try
        {
            final Statement stmt = _conn.createStatement();
            final List<ScriptData> returnData = new LinkedList<ScriptData>();
            final ResultSet rs = stmt.executeQuery("SELECT * FROM SCRIPTS");
            while (rs.next())
            {
                final int id = rs.getInt("ID");
                final String script = rs.getString("SCRIPT");

                returnData.add(new ScriptData(id, script));
            }
            stmt.close();

            return returnData;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            System.out.println("Caught db error: " + e);
        }
        return null;
    }

    public static void updateScript(final ScriptData scriptData)
    {

        try
        {
            final Statement stmt = _conn.createStatement();
            stmt.executeUpdate("UPDATE SCRIPTS SET SCRIPT='" + scriptData.script + "' WHERE ID=" + scriptData.id + ";");
            stmt.close();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            System.out.println("Caught db error: " + e);
        }
    }

    public static ScriptData createScript(final String completedScript)
    {

        try
        {
            final Statement stmt = _conn.createStatement();
            stmt.execute("INSERT INTO SCRIPTS (SCRIPT) VALUES ( '" + completedScript + "' );",
                    Statement.RETURN_GENERATED_KEYS);

            final ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            final int ID = rs.getInt(1);

            System.out.println(ID);
            stmt.close();

            return new ScriptData(ID, completedScript);

        }
        catch (final Exception e)
        {
            e.printStackTrace();
            System.out.println("Caught db error: " + e);
        }
        return null;
    }

    public static void deleteScript(final ScriptData data)
    {
        try
        {
            final Statement stmt = _conn.createStatement();
            stmt.executeUpdate("DELETE FROM SCRIPTS WHERE ID=" + data.id + ";");
            stmt.close();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            System.out.println("Caught db error: " + e);
        }
    }
}

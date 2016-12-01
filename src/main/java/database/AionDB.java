package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import config.ConfigFile;

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
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS PLAYERS(NAME VARCHAR(255), SERVER VARCHAR(255), RACE VARCHAR(255), CLASS VARCHAR(255));");
            stmt.executeUpdate("INSERT INTO PLAYERS VALUES ( 'Claudio', 'Kahrun', 'Asmodian', 'Cleric' )");

            final ResultSet rs = stmt.executeQuery("SELECT * FROM PLAYERS");
            while (rs.next())
            {
                final String name = rs.getString("NAME");
                final String server = rs.getString("SERVER");
                final String race = rs.getString("RACE");
                final String archtype = rs.getString("CLASS");

                System.out.println(name + " " + server + " " + race + " " + archtype);
            }
            stmt.close();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            System.out.println("Error in database");
        }
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
}

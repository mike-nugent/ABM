package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import config.ConfigFile;

public class AionDB 
{
	private Connection _conn;

	public AionDB()
	{
		
	}
	
	public void instantiate()
	{
		try
		{
		    Class.forName("org.h2.Driver");
	        _conn = DriverManager.
	            getConnection("jdbc:h2:file:" + ConfigFile.getOrCreateConfigDir()+"/aion-database", "sa", "");
	         System.out.println("Database connection established...");

            Statement stmt = _conn.createStatement();
            stmt.executeUpdate( "DROP TABLE table1" );
            stmt.executeUpdate( "CREATE TABLE table1 ( user varchar(50) )" );
            stmt.executeUpdate( "INSERT INTO table1 ( user ) VALUES ( 'Claudio' )" );
            stmt.executeUpdate( "INSERT INTO table1 ( user ) VALUES ( 'Bernasconi' )" );
 
            ResultSet rs = stmt.executeQuery("SELECT * FROM table1");
            while( rs.next() )
            {
                String name = rs.getString("user");
                System.out.println( name ); 
            }
            stmt.close();
	            
            _conn.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Error in database");
		}
	}
	
	public void teardown()
	{
		try
		{
			if(_conn != null)
			{
				_conn.close();
			}
		} 
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.out.println("Error in " + this.getClass() + " .teardown()");
		}
	}
}

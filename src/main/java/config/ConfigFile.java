package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import gameinfo.Race;
import gameinfo.Server;

public class ConfigFile
{
    private static final String CONFIG_DIR_NAME  = "aion-battle-meter";
    private static final String CONFIG_FILE_NAME = "config.properties";

    private static final String NAME_KEY     = "name";
    private static final String SERVER_KEY   = "server";
    private static final String RACE_KEY     = "race";
    private static final String AION_DIR_KEY = "aion-install";

    private static File CONFIG_FILE = getOrCreateConfigFile();

    public static final String DEFAULT_AION_LOCATION    = "C:/Program Files (x86)/NCSOFT/Aion";
    public static final String DEFAULT_LOG_FILE_NAME    = "Chat.log";
    public static final String DEFAULT_CFG_FILE_NAME    = "system.cfg";
    public static final String DEBUG_LOG_FILE_NAME      = "TEST_Chat.log";
    public static final String SLIDER_POSITION_PROPERTY = "sliderPosition";
    public static final String STAGE_LOCATION_PROPERTY  = "stageLocation";
    public static final String CLOCK_LOCATION_PROPERTY  = "clockLocation";
    public static final String IS_CLOCK_SHOWING         = "isClockShowing";
    public static final String IS_XFORM_SHOWING         = "isXformShowing";
    public static final String LOCK_WINDOW_POSITION     = "lockWindowPositions";
    public static final String XFORM_LOCATION_PROPERTY  = "xformLocation";
    public static final String UI_SIZES                 = "uiSize";
    public static final String SHOW_ALERTS              = "showAlerts";
    public static final String ALERT_TEXT_COLOR         = "alertTextColor";
    public static final String PVP_LOCATION_PROPERTY    = "pvpLocaion";
    public static final String ARTI_LOCATION_PROPERTY   = "artiLocation";
    public static final String DICE_LOCATION_PROPERTY   = "diceLocation";

    private static String _name;
    private static Race   _race;
    private static Server _server;

    public static File getLogFile()
    {
        String path = getAionInstallLocation();
        if (path == null)
        {
            path = DEFAULT_AION_LOCATION;
        }

        final File file = new File(path + "/" + DEFAULT_LOG_FILE_NAME);
        // final File file = new File(DEBUG_LOG_FILE_NAME);

        return file;
    }

    public static File getCfgFile()
    {
        String path = getAionInstallLocation();
        if (path == null)
        {
            path = DEFAULT_AION_LOCATION;
        }

        final File file = new File(path + "/" + DEFAULT_CFG_FILE_NAME);
        // final File file = new File(DEBUG_LOG_FILE_NAME);

        return file;
    }

    public static String getName()
    {
        if (_name == null)
        {
            _name = getProperty(NAME_KEY);
        }
        return _name;
    }

    public static Server getServer()
    {
        if (_server == null)
        {
            _server = Server.getServer(getProperty(SERVER_KEY));
        }
        return _server;
    }

    public static Race getRace()
    {
        if (_race == null)
        {
            _race = Race.getRace(getProperty(RACE_KEY));
        }
        return _race;
    }

    public static String getAionInstallLocation()
    {
        return getProperty(AION_DIR_KEY);

    }

    public synchronized static void setAionInstallLocation(final String installLocation)
    {
        try
        {
            System.out.println("Writing to file: " + installLocation);
            // Load the properties
            final Properties p = new Properties();
            final FileInputStream fis = new FileInputStream(CONFIG_FILE);
            p.load(fis);
            fis.close();

            // Set the property and save it
            final FileOutputStream fos = new FileOutputStream(CONFIG_FILE);
            p.setProperty(AION_DIR_KEY, installLocation);
            p.store(fos, "Last Modified: " + new Date().toString());
            fos.close();

        }
        catch (final Exception e)
        {
            System.out.println("Error: " + e);
        }
    }

    public synchronized static void setPlayerProperties(final String name, final Server server, final Race race)
    {
        try
        {
            System.out.println("Writing to file: " + name + " " + server + " " + race);
            // Load the properties
            final Properties p = new Properties();
            final FileInputStream fis = new FileInputStream(CONFIG_FILE);
            p.load(fis);
            fis.close();

            // Set the property and save it
            final FileOutputStream fos = new FileOutputStream(CONFIG_FILE);
            p.setProperty(NAME_KEY, name);
            p.setProperty(SERVER_KEY, server.name());
            p.setProperty(RACE_KEY, race.name());
            p.store(fos, "Last Modified: " + new Date().toString());
            fos.close();

            _name = getProperty(NAME_KEY);
            _race = Race.getRace(getProperty(RACE_KEY));
            _server = Server.getServer(getProperty(SERVER_KEY));
        }
        catch (final Exception e)
        {
            System.out.println("Error: " + e);
        }
    }

    public synchronized static void setProperty(final String propertyKey, final String propertyValue)
    {
        try
        {
            System.out.println("Writing to file: " + propertyKey + "=" + propertyValue);
            // Load the properties
            final Properties p = new Properties();
            final FileInputStream fis = new FileInputStream(CONFIG_FILE);
            p.load(fis);
            fis.close();

            // Set the property and save it
            final FileOutputStream fos = new FileOutputStream(CONFIG_FILE);
            p.setProperty(propertyKey, propertyValue);
            p.store(fos, "Last Modified: " + new Date().toString());
            fos.close();
        }
        catch (final Exception e)
        {
            System.out.println("Error: " + e);
        }
    }

    public synchronized static String getProperty(final String key)
    {
        try
        {
            final Properties p = new Properties();
            final FileInputStream fis = new FileInputStream(CONFIG_FILE);
            p.load(fis);
            fis.close();
            return p.getProperty(key);
        }
        catch (final Exception e)
        {
            System.out.println("ERROR: " + e);
            return null;
        }
    }

    public static File getOrCreateConfigFile()
    {
        try
        {
            final File configDir = getOrCreateConfigDir();
            for (final File file : configDir.listFiles())
            {
                if (file.getName().equals(CONFIG_FILE_NAME))
                {
                    System.out.println(file + " file already exists");
                    return file;
                }
            }

            // The config file coul not be found, create it
            final File configFile = new File(configDir.getAbsolutePath() + "/" + CONFIG_FILE_NAME);
            System.out.println("Creating new config file: " + configFile);
            configFile.createNewFile();

            return configFile;
        }
        catch (final Exception e)
        {
            System.out.println("There was a problem, see? " + e);
            return null;
        }
    }

    public static File getOrCreateConfigDir() throws Exception
    {
        final List<File> paths = Arrays.asList(File.listRoots());
        for (final File path : paths)
        {
            if (path.isDirectory())
            {
                for (final File f : path.listFiles())
                {
                    if (f.isDirectory())
                    {
                        if (f.getName().equals(CONFIG_DIR_NAME))
                        {
                            System.out.println("This is the file to use: " + f);
                            return f;
                        }
                    }
                }
            }
        }

        // If no config directory was found on any of the drives.
        final File createNewConfigLocation = new File(getDriveLocation().getAbsolutePath() + CONFIG_DIR_NAME);
        createNewConfigLocation.mkdir();
        if (!createNewConfigLocation.exists())
        {
            System.out.println("Error, could not create the location: " + createNewConfigLocation);
            throw new Exception("Error, could not create the location: " + createNewConfigLocation);
        }
        else if (!createNewConfigLocation.isDirectory())
        {
            System.out.println("Error, was no a directory! " + createNewConfigLocation);
        }

        System.out.println("Creating new file: " + createNewConfigLocation);
        return createNewConfigLocation;
    }

    private static File getDriveLocation() throws Exception
    {
        final List<File> paths = Arrays.asList(File.listRoots());
        System.out.println("The drives available on this computer: " + paths);
        if (paths == null || paths.size() == 0)
        {
            System.out.println("Error, there were no paths detected, should never happen");
            throw new Exception("Error, there were no paths detected, should never happen");
        }

        File rootPath = null;
        for (final File path : paths)
        {
            if (path.getAbsolutePath().equals("C:\\"))
            {
                System.out.println("The C:\\ drive was found, using this");
                rootPath = path;
            }
        }

        // If there was no C directory, then they may have renamed it from C: to
        // something else.
        // Just choose the first one in the list and use that.
        if (rootPath == null)
        {
            for (final File path : paths)
            {
                if (path.canWrite())
                {
                    rootPath = paths.get(0);
                }
            }
        }

        return rootPath;
    }

    public static boolean validateAionPath(final String absolutePath)
    {
        final File f = new File(absolutePath);
        if (!f.exists() || !f.isDirectory())
        {
            System.out.println("ERROR - something went wrong selecting this path: " + absolutePath);
            return false;
        }

        final File configFile = new File(absolutePath + "/" + DEFAULT_LOG_FILE_NAME);
        if (configFile.exists())
        {
            System.out.println("The " + DEFAULT_LOG_FILE_NAME + " file was found! everything looks good here");
            return true;
        }
        else
        {
            System.out.println("The " + DEFAULT_LOG_FILE_NAME + " file was not found.  One needs to be created");
            return false;
        }
    }

    public static boolean isSetup()
    {
        final String name = getName();
        final Server server = getServer();
        final Race race = getRace();
        final String install = getAionInstallLocation();

        if (name == null || name.trim().length() == 0)
        {
            return false;
        }

        if (server == null || server.equals(Server.Unknown))
        {
            return false;
        }

        if (race == null || race.equals(Race.Unknown))
        {
            return false;
        }

        if (install == null || install.trim().length() == 0)
        {
            return false;
        }

        if (!CONFIG_FILE.exists())
        {
            return false;
        }

        return true;
    }
}

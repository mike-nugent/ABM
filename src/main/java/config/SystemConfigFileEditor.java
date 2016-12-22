package config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class SystemConfigFileEditor
{
    /*
     * FOR TESTING: public static void main(final String[] args) { try {
     * enableChatLogFile(); } catch (final Exception e) {
     * System.out.println("Could not modify the file, something went wrong: " +
     * e); e.printStackTrace(); } }
     */

    /**
     * This method will check to see if the chat.log file is enabled.
     *
     * It does this checking for g_chatlog = "1" in system.cfg
     *
     */
    public static boolean isConfigFileEnabled(final String cfgFileInput)
    {
        try
        {
            final String enableLine = getStringFromFile("on.cfg");
            final File systemCFG = new File(cfgFileInput);// ConfigFile.getCfgFile();//

            final String cfgFileTxt = FileUtils.readFileToString(systemCFG, StandardCharsets.ISO_8859_1);
            if (cfgFileTxt.contains(enableLine))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (final Exception e)
        {
            System.out.println("There was an issue, system.cfg file not found:" + e);
            return false;
        }
    }

    public static String getStringFromFile(final String file)
    {
        Scanner sc = null;
        InputStream in = null;
        String returnLine = "";
        try
        {
            in = SystemConfigFileEditor.class.getResourceAsStream(file);
            sc = new Scanner(in, StandardCharsets.ISO_8859_1.name());
            while (sc.hasNextLine())
            {
                final String line = sc.nextLine();
                if (line != null)
                {
                    returnLine += line;
                }
            }
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (final IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (sc != null)
            {
                sc.close();
            }
        }

        return returnLine;
    }

    /**
     * This method will attempt to disable the chat.log file.
     *
     * It does this by decoding the system.cfg file in the Aion install
     * directory, decodes it, and ensures the value g_chatlog = "0" exists
     *
     * If anything goes wrong, and exception is thrown.
     *
     * @throws Exception
     */
    public static void disableChatLogFile(final String cfgFileInput) throws Exception
    {
        // get the system.cfg file of the aion install directory
        final File systemCFG = new File(cfgFileInput);// ConfigFile.getCfgFile();//
        // new
        // File("C:/experiments/off.cfg");

        if (!systemCFG.exists())
        {
            throw new Exception("system.cfg file not found");
        }

        try
        {
            final String enableLine = getStringFromFile("on.cfg");
            final String disableLine = getStringFromFile("off.cfg");

            final String cfgFile = FileUtils.readFileToString(systemCFG, StandardCharsets.ISO_8859_1);
            if (cfgFile.contains(enableLine))
            {
                // In this case, the CFG file had g_chatlog = "0",
                // so change it to g_chatlog = "1"
                System.out.println("Detecting that chat is enabled g_chatlog = \"1\"...");
                System.out.println("...changing value to \"0\"");
                final String replaced = cfgFile.replaceAll(enableLine, disableLine);
                FileUtils.write(systemCFG, replaced, StandardCharsets.ISO_8859_1);
                System.out.println(
                        "Replaced chat file: " + systemCFG.getAbsolutePath() + " chat logging should be enabled now");
            }
            else
            {
                System.out.println("Already contains g_chatlog = \"0\" or isnt configured.");
            }
        }
        catch (final IOException e)
        {
            System.out.println("Caught exception trying to enable chat log file:" + e);
            throw e;
        }
        System.out.println("To enable:  g_chatlog = \"1\"");
        System.out.println("To disable: g_chatlog = \"0\"");
    }

    /**
     * This method will attempt to enable the chat.log file.
     *
     * It does this by decoding the system.cfg file in the Aion install
     * directory, decodes it, and ensures the value g_chatlog = "1" exists
     *
     * If anything goes wrong, and exception is thrown.
     */
    public static void enableChatLogFile(final String cfgFileInput) throws Exception
    {
        // get the system.cfg file of the aion install directory
        final File systemCFG = new File(cfgFileInput);// ConfigFile.getCfgFile();//
        // new
        // File("C:/experiments/off.cfg");

        if (!systemCFG.exists())
        {
            throw new Exception("system.cfg file not found");
        }

        try
        {
            final String enableLine = getStringFromFile("on.cfg");
            final String disableLine = getStringFromFile("off.cfg");

            final String cfgFile = FileUtils.readFileToString(systemCFG, StandardCharsets.ISO_8859_1);
            if (cfgFile.contains(disableLine))
            {
                // In this case, the CFG file had g_chatlog = "0",
                // so change it to g_chatlog = "1"
                System.out.println("Detecting that chat is disabled g_chatlog = \"0\"...");
                System.out.println("...changing value to \"1\"");
                final String replaced = cfgFile.replaceAll(disableLine, enableLine);
                FileUtils.write(systemCFG, replaced, StandardCharsets.ISO_8859_1);
                System.out.println(
                        "Replaced chat file: " + systemCFG.getAbsolutePath() + " chat logging should be enabled now");
            }
            else if (cfgFile.contains(enableLine))
            {
                System.out.println("Already contains g_chatlog = \"1\" line, nothing to do!");
            }
            else
            {
                System.out.println("There was no g_chatlog line found, so adding one at the end.");
                final String addNewLine = cfgFile + enableLine;
                FileUtils.write(systemCFG, addNewLine, StandardCharsets.ISO_8859_1);
                System.out.println(
                        "Replaced chat file: " + systemCFG.getAbsolutePath() + " chat logging should be enabled now");
            }
        }
        catch (final IOException e)
        {
            System.out.println("Caught exception trying to enable chat log file:" + e);
            throw e;
        }
        System.out.println("To enable:  g_chatlog = \"1\"");
        System.out.println("To disable: g_chatlog = \"0\"");
    }
}

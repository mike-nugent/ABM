package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            final ClassLoader classLoader = SystemConfigFileEditor.class.getClassLoader();
            final File EF = new File(classLoader.getResource("configFile/on.cfg").getFile());
            String enableLine = "";
            final File systemCFG = new File(cfgFileInput);// ConfigFile.getCfgFile();//
            // new

            if (!systemCFG.exists())
            {
                System.out.println("There was an issue, system.cfg file not found");
                return false;
            }

            enableLine = FileUtils.readFileToString(EF, StandardCharsets.ISO_8859_1);
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
        // Get the correct encodings saved in resources
        final ClassLoader classLoader = SystemConfigFileEditor.class.getClassLoader();
        final File EF = new File(classLoader.getResource("configFile/on.cfg").getFile());
        final File DF = new File(classLoader.getResource("configFile/off.cfg").getFile());

        // These will hold the encoded values for g_chatlog = "1"
        String enableLine = "";
        String disableLine = "";

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
            enableLine = FileUtils.readFileToString(EF, StandardCharsets.ISO_8859_1);
            disableLine = FileUtils.readFileToString(DF, StandardCharsets.ISO_8859_1);

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

    /**
     * This method writes encoded files ignoring irregularities in the encoding.
     *
     * @param A
     *            from
     * @param B
     *            to
     * @throws IOException
     */
    public static void writeToNewFile(final String A, final String B) throws IOException
    {

        final Path in = Paths.get(A);
        final Path out = Paths.get(B);

        final CharsetDecoder decoder = StandardCharsets.ISO_8859_1.newDecoder();
        decoder.onMalformedInput(CodingErrorAction.IGNORE);
        decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(in.toFile()), decoder));

        final CharsetEncoder encoder = StandardCharsets.ISO_8859_1.newEncoder();
        encoder.onMalformedInput(CodingErrorAction.IGNORE);
        encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

        final BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(out.toFile()), encoder));

        final char[] charBuffer = new char[100];
        int readCharCount;
        final StringBuffer buffer = new StringBuffer();

        while ((readCharCount = reader.read(charBuffer)) > 0)
        {
            buffer.append(charBuffer, 0, readCharCount);
            // here goes more code to process the content
            // buffer must be written to output on each iteration
        }

        writer.write(buffer.toString());
        reader.close();
        writer.close();
    }

}

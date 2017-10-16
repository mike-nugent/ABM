package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

public class RUNNER_Populate_All_Icons
{

    public static void main(final String[] args)
    {
        final File allItemsDir = new File("skills");
        System.out.println(allItemsDir.getAbsolutePath());
        allItemsDir.mkdirs();

        final int total = 200000000;
        // 000,000,000
        for (int i = 0; i < total; i++)
        {
            String str = i + "";
            if (str.length() < 9)
            {
                str = make("0", 9 - str.length()) + str;
            }
            final String readUrlStr = readUrl(str);
            if (readUrlStr.contains("skillId"))
            {
                System.out.println("\n\n " + ((double) i / (double) total) + " \n\n");
                System.out.println(str);
                System.out.println(readUrlStr);
                final File f = new File(allItemsDir.getAbsolutePath() + "/" + str + ".json");
                if (!f.exists())
                {
                    try
                    {
                        FileUtils.write(f, readUrlStr, Charset.forName("UTF-8"));
                    }
                    catch (final IOException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                System.out.println(i);
            }

        }
    }

    private static String make(final String string, final int count)
    {
        String ret = "";
        for (int i = 0; i < count; i++)
        {
            ret += string;
        }
        return ret;
    }

    // TODO - use this info in a bit. For now just icons are fine.
    private static String readUrl(final String itemID)
    {
        BufferedReader reader = null;
        try
        {
            final URL url = new URL("http://api.notaion.com/?skill&id=" + itemID);
            final URLConnection conn = url.openConnection();

            // The not aion API server refuses requests that aren't made by browser. So trick it into thinking we are a browser.
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer buffer = new StringBuffer();
            int read;
            final char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
            {
                buffer.append(chars, 0, read);
            }

            return buffer.toString();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            return "";
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (final IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}

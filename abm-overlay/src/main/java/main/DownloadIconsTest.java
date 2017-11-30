package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DownloadIconsTest
{
    public static void main(final String[] args)
    {
        try
        {
            final String result = readUrl("http://api.notaion.com/?icon&id=182007000");
            System.out.println(result);
        }
        catch (final Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String readUrl(final String urlString) throws Exception
    {
        BufferedReader reader = null;
        try
        {
            final URL url = new URL(urlString);
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
        finally
        {
            if (reader != null)
            {
                reader.close();
            }
        }
    }
}

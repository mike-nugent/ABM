package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

public class RUNNER_Combine_Item_Files
{
    public static void main(final String[] args)
    {
        final File allItemsDir = new File("items");
        final File combinedfile = new File("items/all-items.txt");
        System.out.println(allItemsDir.getAbsolutePath());
        System.out.println(combinedfile.getAbsolutePath());
        allItemsDir.mkdirs();

        combinedfile.delete();

        try
        {
            combinedfile.createNewFile();
        }
        catch (final IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        PrintWriter fstream = null;
        try
        {
            fstream = new PrintWriter(new FileWriter(combinedfile));
        }
        catch (final IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        final File[] files = allItemsDir.listFiles();
        for (final File f : files)
        {
            try
            {
                final String in = FileUtils.readFileToString(f, Charset.forName("UTF-8"));
                fstream.println(in);
                fstream.flush();
                System.out.println(in);
            }
            catch (final IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(f);
        }
    }
}

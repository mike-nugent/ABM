package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EBLootParser
{

    public static void main(final String[] args)
    {
        final File inFile = new File("C:/Users/Mike/Desktop/EB-loot-list.txt");
        FileInputStream inputStream = null;
        Scanner sc = null;

        final Map<String, Integer> lootList = new HashMap<String, Integer>();

        try
        {
            inputStream = new FileInputStream(inFile);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine())
            {
                final String line = sc.nextLine();
                if (line.contains(" : "))
                {
                    System.out.println("Attempting to parse: " + line);
                    String person = line.substring(line.indexOf(" : ") + 3, line.indexOf("acquired"));
                    person = person.replace(" has", "");
                    person = person.replaceAll(" have", "");
                    person = person.trim();
                    if (person.equals("You"))
                    {
                        person = "Zhule";
                    }

                    if (lootList.containsKey(person))
                    {
                        Integer count = lootList.get(person);
                        count = count.intValue() + 1;
                        lootList.put(person, count);
                    }
                    else
                    {
                        lootList.put(person, 1);
                    }
                }
            }

            for (final String key : lootList.keySet())
            {
                final String name = key;
                final int value = lootList.get(key).intValue();

                System.out.println(name + " " + make("X", value));
            }
        }
        catch (final FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
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
    }

    public static String make(final String s, final int times)
    {
        String ret = "";

        for (int i = 0; i < times; i++)
        {
            ret += (s + " ");
        }

        return ret.trim();
    }
}

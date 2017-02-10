package loot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gameinfo.IconLoader;
import javafx.scene.image.ImageView;

public class ItemData
{
    private final ImageView image;
    private final String    itemID;
    private String          quality;
    private String          itemName;
    private String          type;
    private final String    json;

    public ItemData(final String itemID)
    {
        this.itemID = itemID;
        image = IconLoader.getItemIcon(itemID);
        json = readUrl(itemID);
        try
        {
            if (json != null)
            {
                final JSONObject obj = parseJson(json);
                if (obj != null)
                {
                    final JSONArray item = obj.getJSONArray("item");
                    final JSONObject desc = item.getJSONObject(0);
                    quality = desc.getString("quality");
                    itemName = desc.getString("name");
                    type = desc.getString("type");
                }
            }
        }
        catch (final JSONException e)
        {
            System.out.println("Issue parsing item: " + itemID);
            e.printStackTrace();
        }
    }

    public ImageView getImage()
    {
        return image;
    }

    public String getItemName()
    {
        return itemName;
    }

    public String getItemType()
    {
        return type;
    }

    public String getItemQuality()
    {
        return quality;
    }

    public String getJson()
    {
        return json;
    }

    private JSONObject parseJson(final String json)
    {
        try
        {
            return new JSONObject(json);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    // TODO - use this info in a bit. For now just icons are fine.
    private static String readUrl(final String itemID)
    {
        BufferedReader reader = null;
        try
        {
            final URL url = new URL("http://api.notaion.com/?item&id=" + itemID);
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

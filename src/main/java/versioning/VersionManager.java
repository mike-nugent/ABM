package versioning;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import fx.screens.CustomAlert;
import fx.screens.UpdateAvailableAlert;
import main.MainFX;

public class VersionManager 
{
	
	//VERSION
	public static final String CURRENT_VERSION = MainFX.CURRENT_VERSION;
	public static String AVAILABLE_VERSION = getAvailableVersion("http://yarhar.com/aion/current.version");
	private static UpdateAvailableAlert alert;

	
    private static String getAvailableVersion(String address)
    {
    	try 
    	{
			URL url = new URL(address);
			
			// read text returned by server
		    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		    
		    String returnStr = "";
		    String line;
		    while ((line = in.readLine()) != null) 
		    {
		    	System.out.println(line);
		    	returnStr+=line;
		    }
		    in.close();
		    
		    if(returnStr != null && returnStr.contains("version=[") && returnStr.contains("]"))
	    	{
	    		System.out.println(returnStr);
	    		returnStr = returnStr.substring(returnStr.indexOf("version=[")+9, returnStr.indexOf("]"));
	    	}
		    return returnStr;
		}
		catch (Exception e)
    	{
			System.out.println("Malformed URL: " + e.getMessage());
			return "[unknown]";
		}
    }
    
    /**
     * Compares two version strings. 
     * 
     * Use this instead of String.compareTo() for a non-lexicographical 
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     * 
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     * 
     * @param str1 a string of ordinal numbers separated by decimal points. 
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2. 
     *         The result is a positive integer if str1 is _numerically_ greater than str2. 
     *         The result is zero if the strings are _numerically_ equal.
     */
    public static int versionCompare(String str1, String str2) 
    {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
          i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }

    
    public static boolean checkForNewerVersions() 
    {    	
    	try
    	{	    	
	    	System.out.println("Current Version:   " + CURRENT_VERSION);
	    	System.out.println("Available Version: " + AVAILABLE_VERSION);
	    	
	    	
	    	int versionCompare = versionCompare(CURRENT_VERSION, AVAILABLE_VERSION);
	    	if(versionCompare < 0)
	    	{
	    		if(alert == null)
	    		{
		    		 alert = new UpdateAvailableAlert("New Version Available", 
		    				"There is newer version of ASDM available for download.",
		    				CURRENT_VERSION, AVAILABLE_VERSION);
	    		}

	    		alert.show();
	    		return true;
	    	}
	    	else
	    	{
	    		return false;
	    	}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		System.out.println("There was a problem checking the latest version");
    		
    		CustomAlert alert = new CustomAlert("Minor Issue Detected", "Could not detect if updates were available or not.");
    		alert.show();

    		return true;
    	}
	}
}

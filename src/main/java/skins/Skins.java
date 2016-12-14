package skins;

public class Skins
{
    public static String get(final String script)
    {
        return Skins.class.getResource(script).toExternalForm();
    }

}

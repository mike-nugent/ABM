package gameinfo;

public class SoundData
{
    public String path;
    public int    id;
    public String name;

    public SoundData(final int id, final String name, final String path)
    {
        this.path = path;
        this.name = name;
        this.id = id;
    }
}

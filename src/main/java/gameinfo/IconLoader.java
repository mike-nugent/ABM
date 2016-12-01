package gameinfo;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class IconLoader
{
    public static final int ICON_SIZE = 40;

    public static ImageIcon aethertech      = loadIcon(Archetype.Aethertech);
    public static ImageIcon assassin        = loadIcon(Archetype.Assassin);
    public static ImageIcon chanter         = loadIcon(Archetype.Chanter);
    public static ImageIcon cleric          = loadIcon(Archetype.Cleric);
    public static ImageIcon gladiator       = loadIcon(Archetype.Gladiator);
    public static ImageIcon gunslinger      = loadIcon(Archetype.Gunslinger);
    public static ImageIcon ranger          = loadIcon(Archetype.Ranger);
    public static ImageIcon songweaver      = loadIcon(Archetype.Songweaver);
    public static ImageIcon sorcerer        = loadIcon(Archetype.Sorcerer);
    public static ImageIcon spiritmaster    = loadIcon(Archetype.Spiritmaster);
    public static ImageIcon templar         = loadIcon(Archetype.Templar);
    public static ImageIcon unknown         = loadIcon(Archetype.Unknown);
    public static ImageIcon transform       = loadSpecial("transform_icon.jpg", 45);
    public static ImageIcon tray_icon       = loadSpecial("transform_icon.jpg", 16);
    public static ImageIcon elyos           = loadSpecial("Elyos-emblem.png", 45);
    public static ImageIcon asmodian        = loadSpecial("Asmodian-emblem.png", 45);
    public static ImageIcon faction         = loadSpecial("faction2.png", 45);
    public static ImageIcon shugos          = loadSpecial("aion_lore.png", 45);
    public static ImageIcon hacking         = loadSpecial("hacking.png", 45);
    public static ImageIcon atrea           = loadSpecial("aion_area.png", 45);
    public static ImageIcon artifact        = loadSpecial("artifact.png", 45);
    public static ImageIcon config          = loadSpecial("config.png", 45);
    public static ImageIcon reshanta        = loadSpecial("reshanta.png", 800);
    public static ImageIcon artifactFire    = loadSpecial("artifact-fire.png", 40);
    public static ImageIcon artifactCaptain = loadSpecial("artifact-captain.png", 32);
    public static ImageIcon check           = loadSpecial("check.png", 20);
    public static ImageIcon warning         = loadSpecial("warning.png", 20);
    public static ImageIcon modified        = loadSpecial("modified.png", 20);

    private static ImageIcon loadIcon(final Archetype cz)
    {
        try
        {
            final InputStream in = IconLoader.class.getResourceAsStream("/" + cz.getIconName());

            return new ImageIcon(ImageIO.read(in).getScaledInstance(-1, ICON_SIZE, Image.SCALE_SMOOTH));
        }
        catch (final IOException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public static javafx.scene.image.Image loadFxImage(final String icon)
    {
        return loadFxImage(icon, ICON_SIZE);
    }

    public static javafx.scene.image.Image loadFxImage(final String icon, final int scaleHeight)
    {
        try
        {
            final InputStream in = IconLoader.class.getResourceAsStream("/" + icon);

            return new javafx.scene.image.Image(in, -1, scaleHeight, true, true);

        }
        catch (final Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    private static ImageIcon loadSpecial(final String icon, final int scaleHeight)
    {
        try
        {
            final InputStream in = IconLoader.class.getResourceAsStream("/" + icon);

            return new ImageIcon(ImageIO.read(in).getScaledInstance(-1, scaleHeight, Image.SCALE_SMOOTH));
        }
        catch (final IOException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public static ImageIcon getIcon(final Archetype cz)
    {
        switch (cz)
        {
            case Aethertech:
                return aethertech;
            case Assassin:
                return assassin;
            case Chanter:
                return chanter;
            case Cleric:
                return cleric;
            case Gladiator:
                return gladiator;
            case Gunslinger:
                return gunslinger;
            case Ranger:
                return ranger;
            case Songweaver:
                return songweaver;
            case Sorcerer:
                return sorcerer;
            case Spiritmaster:
                return spiritmaster;
            case Templar:
                return templar;
            case Unknown:
            default:
                return unknown;
        }
    }
}

package sounds;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import database.AionDB;
import fx.screens.SoundBar;
import fx.screens.SoundsScreen;
import gameinfo.SoundData;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import triage.ExceptionHandler;

public class SoundManager
{
    private static List<String>    embeddedSounds = null;
    private static List<SoundData> customSounds;

    public static void initialize()
    {
        customSounds = AionDB.getAllCustomSounds();
    }

    public static SoundData createNewCustomSound(final String name, final String path)
    {
        final SoundData data = AionDB.createSound(name, path);
        customSounds.add(data);
        SoundsScreen.newSoundAdded(data);
        return data;
    }

    public static List<String> getEmbeddedSounds()
    {
        if (embeddedSounds == null)
        {
            try
            {

                embeddedSounds = new ArrayList<String>();
                final String[] files = getResourceListing(SoundManager.class, "sounds/");
                for (final String f : files)
                {
                    if (f.endsWith(".wav"))
                    {
                        embeddedSounds.add(f);
                    }
                }
            }
            catch (final Exception e)
            {
                ExceptionHandler.handleException(e);
            }
        }

        return embeddedSounds;
    }

    public static List<SoundData> getCustomSounds()
    {
        return customSounds;
    }

    private static String[] getResourceListing(final Class clazz, final String path)
            throws URISyntaxException, IOException
    {
        URL dirURL = clazz.getClassLoader().getResource(path);
        if (dirURL != null && dirURL.getProtocol().equals("file"))
        {
            /* A file path: easy enough */
            return new File(dirURL.toURI()).list();
        }

        if (dirURL == null)
        {
            /*
             * In case of a jar file, we can't actually find a directory. Have
             * to assume the same jar as clazz.
             */
            final String me = clazz.getName().replace(".", "/") + ".class";
            dirURL = clazz.getClassLoader().getResource(me);
        }

        if (dirURL.getProtocol().equals("jar"))
        {
            /* A JAR path */
            final String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); // strip
                                                                                                 // out
                                                                                                 // only
                                                                                                 // the
                                                                                                 // JAR
                                                                                                 // file
            final JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            final Enumeration<JarEntry> entries = jar.entries(); // gives ALL
                                                                 // entries in
                                                                 // jar
            final Set<String> result = new HashSet<String>(); // avoid
                                                              // duplicates in
                                                              // case it is a
                                                              // subdirectory
            while (entries.hasMoreElements())
            {
                final String name = entries.nextElement().getName();
                if (name.startsWith(path))
                { // filter according to the path
                    String entry = name.substring(path.length());
                    final int checkSubdir = entry.indexOf("/");
                    if (checkSubdir >= 0)
                    {
                        // if it is a subdirectory, we just return the directory
                        // name
                        entry = entry.substring(0, checkSubdir);
                    }
                    result.add(entry);
                }
            }
            return result.toArray(new String[result.size()]);
        }

        throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
    }

    public static synchronized void playTransformSound()
    {
        new Thread(new Runnable()
        {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            @Override
            public void run()
            {
                try
                {
                    final Clip clip = AudioSystem.getClip();
                    final URL url = getClass().getResource("fart_z.wav");
                    final AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                    clip.open(ais);
                    clip.start();

                }
                catch (final Exception e)
                {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static synchronized void playTickSound()
    {
        new Thread(new Runnable()
        {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            @Override
            public void run()
            {
                try
                {
                    final Clip clip = AudioSystem.getClip();
                    final URL url = getClass().getResource("click.wav");
                    final AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                    clip.open(ais);
                    clip.start();

                }
                catch (final Exception e)
                {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static synchronized void playBuzzerSound()
    {
        new Thread(new Runnable()
        {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            @Override
            public void run()
            {
                try
                {
                    final Clip clip = AudioSystem.getClip();
                    final URL url = getClass().getResource("click.wav");
                    final AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                    clip.open(ais);
                    clip.start();

                }
                catch (final Exception e)
                {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static void playEmbeddedSound(final String fileName)
    {
        new Thread(new Runnable()
        {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            @Override
            public void run()
            {
                try
                {
                    final Clip clip = AudioSystem.getClip();
                    final URL url = getClass().getResource(fileName);
                    final AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                    clip.open(ais);
                    clip.start();

                }
                catch (final Exception e)
                {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static void playLocalSound(final SoundData data)
    {
        new Thread(new Runnable()
        {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            @Override
            public void run()
            {
                try
                {
                    final Media sound = new Media(new File(data.path).toURI().toString());
                    final MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                }
                catch (final Exception e)
                {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
        }).start();

    }

    public static void deleteCustomSound(final SoundData data, final SoundBar bar)
    {
        final boolean wasDeleted = SoundsScreen.deleteCustomSound(data, bar);
        if (wasDeleted)
        {
            AionDB.deleteSound(data);
            customSounds.remove(data);
        }
    }

}

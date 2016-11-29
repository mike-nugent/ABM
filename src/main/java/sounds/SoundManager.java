package sounds;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager
{
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
                    final URL url = getClass().getResource("buzzer.wav");
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

}

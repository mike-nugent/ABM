package sounds;

import java.net.URL;
import java.util.Arrays;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

public class SoundManager 
{
	public static synchronized void playTransformSound()
	{
		new Thread(new Runnable()
		{
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run() 
			{
				try 
				{
					Clip clip = AudioSystem.getClip();
					URL url = getClass().getResource("fart_z.wav");
					AudioInputStream ais = AudioSystem.getAudioInputStream(url);
					clip.open(ais);
					clip.start();

				}
				catch (Exception e) 
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
			public void run() 
			{
				try 
				{
					Clip clip = AudioSystem.getClip();
					URL url = getClass().getResource("click.wav");
					AudioInputStream ais = AudioSystem.getAudioInputStream(url);
					clip.open(ais);
					clip.start();

				}
				catch (Exception e) 
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
			public void run() 
			{
				try 
				{
					Clip clip = AudioSystem.getClip();
					URL url = getClass().getResource("buzzer.wav");
					AudioInputStream ais = AudioSystem.getAudioInputStream(url);
					clip.open(ais);
					clip.start();
				}
				catch (Exception e) 
				{
					e.printStackTrace();
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}
	
}

package areas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import sounds.SoundManager;
import utils.Times;

public abstract class BaseTimer extends JPanel implements ActionListener
{
	protected static final long FIRST_WAVE = 55000;
	protected static final long SECOND_WAVE = 205000;
	protected static final long THIRD_WAVE = 355000;

	private Map<String, String> _originalSoundMap;
	private Map<String, String> _currentSoundMap;

	protected JLabel _ms;
	protected JLabel _milli;
	private JButton _startStop;
	private JButton _reset;
	
	private boolean _stopFlag = true;
	protected long _startTime = 0;
	private String clockStartTxt;
	 
	
	public BaseTimer(String clockStartTxt, String infoTxt)
	{
		this.clockStartTxt = clockStartTxt;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.setAlignmentX(LEFT_ALIGNMENT);
		JPanel intoWrapper = new JPanel();

		intoWrapper.setLayout(new BoxLayout(intoWrapper, BoxLayout.PAGE_AXIS));
		intoWrapper.setAlignmentX(LEFT_ALIGNMENT);
		JTextArea area = new JTextArea(infoTxt);
		area.setEditable(false);
		intoWrapper.add(area);
		this.add(intoWrapper);

		_ms = new JLabel(clockStartTxt);
		_milli = new JLabel("00");
		
		_ms.setAlignmentX(LEFT_ALIGNMENT);
		_milli.setAlignmentX(LEFT_ALIGNMENT);

		_ms.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 140));
		_milli.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
		JPanel clockWrapper = new JPanel();
		clockWrapper.setBackground(Color.white);
		clockWrapper.setAlignmentX(LEFT_ALIGNMENT);
		clockWrapper.add(_ms);
		clockWrapper.add(_milli);
		this.add(clockWrapper);

		_startStop = new JButton("Start");
		_reset = new JButton("Reset");
		
		_startStop.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 30));
		_reset.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 30));
		
		_startStop.addActionListener(this);
		_reset.addActionListener(this);
		
		_startStop.setAlignmentX(LEFT_ALIGNMENT);
		_reset.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel btnWrapper = new JPanel();
		btnWrapper.setBackground(Color.white);
		btnWrapper.setAlignmentX(LEFT_ALIGNMENT);
		btnWrapper.add(_startStop);
		btnWrapper.add(_reset);
		this.add(btnWrapper);
	}
	
	
	private Map<String, String> buildSoundMap() 
	{
		if(_originalSoundMap != null)
		{
			Map<String, String> copiedMap = new HashMap<String, String>(_originalSoundMap);
			return copiedMap;	
		}
		else
		{
			return new HashMap<String, String>();
		}
	}
	
	public void setSoundMap(Map<String,String> map)
	{
		_originalSoundMap = map;
	}
	
	
	protected abstract void timer();
	
	
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == _startStop)
		{
			SoundManager.playTickSound();
			_currentSoundMap = buildSoundMap();
			if(_startStop.getText().equals("Start"))
			{
				_startTime = System.currentTimeMillis();
				_startStop.setText("Stop");
				_stopFlag = false;
				
				
				Runnable r = new Runnable() 
				{
					public void run() 
					{
						try 
						{
							timer();
						} 
						catch (Exception x) 
						{
							x.printStackTrace();
						}
					}
				};

				Thread internalThread = new Thread(r);
				internalThread.start();
				
				
			}
			else
			{
				_stopFlag = true;
				_startStop.setEnabled(false);
				_startStop.setText("Start");
			}
		}
		else if (e.getSource() == _reset)
		{
			SoundManager.playTickSound();
			_startStop.setText("Start");
			_startStop.setEnabled(true);
			_stopFlag = true;
			_ms.setText(clockStartTxt);
			_milli.setText("00");
			_currentSoundMap = buildSoundMap();
			_ms.setForeground(Color.black);
			_milli.setForeground(Color.black);
		}
	}
	



	protected void playSoundIfNeeded(String time) 
	{
		//time is in the format 00:00
		
		if(_currentSoundMap.containsKey(time))
		{
			String B = "B";
			String A = "A";
			
			String sound = _currentSoundMap.get(time);
			if(sound.equals(A))
			{
				SoundManager.playBuzzerSound();
			}
			else if (sound.equals(B))
			{
				SoundManager.playTickSound();
			}
			_currentSoundMap.remove(time);
		}
	}


	protected abstract Color getColorBasedOnTime(long diff);
		

	protected boolean isStopped() 
	{
		return _stopFlag;
	}
}

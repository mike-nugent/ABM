package config;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;

import gameinfo.IconLoader;
import gameinfo.PlayerData;
import gameinfo.Race;
import gameinfo.Server;
import main.MainSwing;
import utils.AionLogFileFilter;
import utils.WrapLayout;

public class ConfigJPanel extends JPanel implements ActionListener, KeyListener
{

	private JButton _configureBtn;
	private MainSwing _main;
	private JTextComponent _aionBox;
	private JLabel nameLabel;
	private JLabel serverLabel;
	private JLabel raceLabel;
	private JLabel aionInstallLabel;
	private JComboBox<Server> serverBox;
	private JComboBox<Race> raceBox;
	private JTextField nameText;
	private JButton saveConfiguration;  
	private String _oldName; //special case so we dont read a bunch of times on event;

	public ConfigJPanel(MainSwing main) 
	{
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		_main = main; 	
		
    	makePanel();
    	makePanel();
    	JPanel panel = makePanel();
    	JPanel update = makePanel();
    	makePanel();

    	JPanel fields = createFields();
    	panel.add(fields);
    	
    	saveConfiguration = new JButton("Save Changes");
    	saveConfiguration.addActionListener(this);
    	saveConfiguration.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 30));
    	saveConfiguration.setEnabled(false);;
    	update.add(saveConfiguration);
	}

	private JPanel makePanel() 
	{
		JPanel panel3 = new JPanel(new WrapLayout());
		//panel3.setBorder(BorderFactory.createLineBorder(Color.black));
		add(panel3);
		return panel3;
	}

	private JPanel createFields() 
	{
    	Font F = new Font(Font.DIALOG, Font.BOLD, 18);
    	JPanel p = new JPanel(new GridLayout(0, 2, 5, 5)); 
    	nameLabel = new JLabel("The name of your character: ", SwingConstants.RIGHT);
    	serverLabel = new JLabel("The server you play on: ", JLabel.TRAILING);
    	raceLabel = new JLabel("Your race: ", JLabel.TRAILING);
    	aionInstallLabel = new JLabel("Where Aion is installed: ", JLabel.TRAILING);
    	nameLabel.setFont(F);
    	serverLabel.setFont(F);
    	raceLabel.setFont(F);
    	aionInstallLabel.setFont(F);
    	
    	
    	nameText = new JTextField(28);
    	nameText.addKeyListener(this);
    	nameText.setText(ConfigFile.getName());
    	_oldName = nameText.getText() == null ? "" : nameText.getText();
    	if(nameText.getText() == null || nameText.getText().trim().length() == 0)
    	{
    		nameLabel.setIcon(IconLoader.warning);
    	}
    	else
    	{
    		nameLabel.setIcon(IconLoader.check);
    	}
    	
    	serverBox = new JComboBox<Server>(Server.getLegalValues());
    	Server currentServer = ConfigFile.getServer();
    	if(currentServer.equals(Server.Unknown))
    		currentServer = null;
    	serverBox.setSelectedItem(currentServer);
    	if(serverBox.getSelectedItem() == null)
    	{
    		serverLabel.setIcon(IconLoader.warning);
    	}
    	else
    	{
    		serverLabel.setIcon(IconLoader.check);
    	}
    	serverBox.addActionListener(this);
    	

    	raceBox = new JComboBox<Race>(Race.getLegalValues());
    	Race currentRace = ConfigFile.getRace();
    	if(currentRace.equals(Race.Unknown))
    		currentRace = null;
    	raceBox.setSelectedItem(currentRace);
    	if(raceBox.getSelectedItem() == null)
    	{
    		raceLabel.setIcon(IconLoader.warning);
    	}
    	else
    	{
    		raceLabel.setIcon(IconLoader.check);
    	}
    	raceBox.addActionListener(this);

    	JPanel selectionPanel = new JPanel();
    	FlowLayout layout = (FlowLayout) selectionPanel.getLayout();
    	layout.setVgap(0); 
    	layout.setHgap(0);
    	
    	_aionBox = new JTextField(ConfigFile.getAionInstallLocation(), 25);
    	
    	if(_aionBox.getText() == null || _aionBox.getText().trim().length() == 0)
    	{
    		//Check to see if we can find it ourselves.
            boolean chatFileExists = ConfigFile.validateAionPath(ConfigFile.DEFAULT_LOG_FILE_LOCATION);
            if(chatFileExists)
            {
        		aionInstallLabel.setIcon(IconLoader.check);
        		_aionBox.setText(ConfigFile.DEFAULT_LOG_FILE_LOCATION);
            }
            else
            {
        		aionInstallLabel.setIcon(IconLoader.warning);
            }
    	}
    	else
    	{
    		aionInstallLabel.setIcon(IconLoader.check);
    	}
    	_aionBox.setEditable(false);
    	
    	_configureBtn = new JButton("Configure...");
    	_configureBtn.addActionListener(this);
    	selectionPanel.add(_aionBox);
    	selectionPanel.add(_configureBtn);
    	
    	nameLabel.setLabelFor(nameText);
    	serverLabel.setLabelFor(serverBox);
    	raceLabel.setLabelFor(raceBox);
    	aionInstallLabel.setLabelFor(selectionPanel);
    	
    	p.add(nameLabel);
    	p.add(nameText);
    	
    	p.add(serverLabel);
    	p.add(serverBox);
    	
    	p.add(raceLabel);
    	p.add(raceBox);
    	
    	p.add(aionInstallLabel);
    	p.add(selectionPanel);
    	//p.setBackground(Color.darkGray);
    	
    	return p;
	}
	
	private void updateSaveChangesBox() 
	{
		String oldName = ConfigFile.getName() == null ? "" : ConfigFile.getName();
		Race oldRace = ConfigFile.getRace() == null ? Race.Unknown : ConfigFile.getRace();
		Server oldServer = ConfigFile.getServer();
		String oldPath = ConfigFile.getAionInstallLocation() == null ? "" : ConfigFile.getAionInstallLocation();
		
		String newName = nameText.getText();
		Race newRace = (Race) raceBox.getSelectedItem();
		Server newServer = (Server) serverBox.getSelectedItem();
		String newPath = _aionBox.getText();
		
		boolean A = !oldName.equals(newName);
		boolean B = !oldRace.equals(newRace);
		boolean C = !oldServer.equals(newServer);
		boolean D = !oldPath.equals(newPath);

		boolean all_leagal = false;
		//Only allow saving if all checkmarks are green
		Icon check = IconLoader.check;
		Icon modified = IconLoader.modified;

		Icon nameIcon = nameLabel.getIcon();
		Icon raceIcon = raceLabel.getIcon();
		Icon serverIcon = serverLabel.getIcon();
		Icon locationIcon = aionInstallLabel.getIcon();
		
		boolean nameOK = check.equals(nameIcon) || modified.equals(nameIcon);
		boolean raceOK = check.equals(raceIcon) || modified.equals(raceIcon);
		boolean serverOK = check.equals(serverIcon) || modified.equals(serverIcon);
		boolean installOK = check.equals(locationIcon) || modified.equals(locationIcon);

		
		
		if(nameOK && raceOK && serverOK && installOK)
		{
			all_leagal = true;
		}
		
		
		if(A || B || C || D)
		{
			if(all_leagal)
			{
				saveConfiguration.setText("Save Changes");
		    	saveConfiguration.setEnabled(true);
			}
			else
			{
		    	saveConfiguration.setEnabled(false);
			}
		}

	}

	public void actionPerformed(ActionEvent e) 
	{

		if(e.getSource() == _configureBtn)
		{
			System.out.println("Button clicked");
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(ConfigFile.DEFAULT_LOG_FILE_LOCATION));
			fc.setDialogTitle("Locate the Chat.log file in your Aion installation directory");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setAcceptAllFileFilterUsed(false);
			FileFilter filter = new AionLogFileFilter();
			fc.setFileFilter(filter);
			int returnVal = fc.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) 
	        {
	            File file = fc.getSelectedFile();
	            System.out.println(file.getAbsolutePath());
	            _aionBox.setText(file.getAbsolutePath());

	            boolean chatFileExists = ConfigFile.validateAionPath(file.getAbsolutePath());
	            if(!chatFileExists)
	            {
	    			aionInstallLabel.setIcon(IconLoader.warning);

	            	//prompt user that one needs to be created.
	    			JOptionPane.showMessageDialog(_main.getFrame(),
	    				    "The Aion log file could not be detected.\n" + 
	    				    "One will need to be created and the Aion client restarted.\n\n"
	    				    + "To fix this, complete the following steps:\n"
	    				    + "1) Open the directory: " + file.getAbsolutePath() + "\n"
	    				   	+ "2) Create a new file called: " + ConfigFile.DEFAULT_LOG_FILE_NAME +"\n"
	    				    + "3) Restart your Aion client, and restart this program.\n\n",
	    				    "Aion Chat.log file not found!",
	    				    JOptionPane.INFORMATION_MESSAGE, IconLoader.warning);	
	    			
	            }
	            else
	            {
	            	aionInstallLabel.setIcon(IconLoader.check);
	            }
	            //ConfigFile.updateLogFileLocation(file.getAbsolutePath());
	            //_main.startLoggers();
	        } 
	        else 
	        {
	            System.out.println("Canceled by user");
	        }
			updateSaveChangesBox();
		}
		else if (e.getSource() == serverBox)
		{
			Server newServer = (Server) serverBox.getSelectedItem();
			if(newServer != null)
			{
				Server old = ConfigFile.getServer();
				if(newServer.equals(old))
				{
					serverLabel.setIcon(IconLoader.check);
				}
				else
				{
					serverLabel.setIcon(IconLoader.modified);
				}
			}
			else
			{
				serverLabel.setIcon(IconLoader.warning);
			}
			updateSaveChangesBox();
		}
		else if (e.getSource() == raceBox)
		{
			Race newRace = (Race) raceBox.getSelectedItem();
			if(raceBox.getSelectedItem() != null)
			{
				Race old = ConfigFile.getRace();
				if(newRace.equals(old))
				{
					raceLabel.setIcon(IconLoader.check);
				}
				else
				{
					raceLabel.setIcon(IconLoader.modified);
				}			}
			else
			{
				raceLabel.setIcon(IconLoader.warning);
			}
			updateSaveChangesBox();
		}
		else if (e.getSource() == saveConfiguration)
		{
			//Save button clicked.
			String name = nameText.getText();
			Server server = (Server) serverBox.getSelectedItem();
			Race race = (Race) raceBox.getSelectedItem();
			//String installLocation = _aionBox.getText();
			ConfigFile.setPlayerProperties(name, server, race);
	    	saveConfiguration.setEnabled(false);
	    	_oldName = name;
	    	nameLabel.setIcon(IconLoader.check);
	    	raceLabel.setIcon(IconLoader.check);
	    	serverLabel.setIcon(IconLoader.check);
	    	aionInstallLabel.setIcon(IconLoader.check);
	    	saveConfiguration.setText("Changes Saved");
	    	
	    	_main.startLoggers();
		}
		else
		{
			//random btn clicked;
			PlayerData p = PlayerData.generateRandom();
			_main.transformDetected(p);
		}
		
	}

	public void keyTyped(KeyEvent e) 
	{
		JTextField txt = (JTextField) e.getComponent();
		if(txt != null)
		{
			String newName = txt.getText();
			if(newName != null &&newName.trim().length() > 0 )
			{
				if(newName.equals(_oldName))
				{
					nameLabel.setIcon(IconLoader.check);
				}
				else
				{
					nameLabel.setIcon(IconLoader.modified);
				}
			}
			else
			{
				nameLabel.setIcon(IconLoader.warning);
			}
			updateSaveChangesBox();
		}
	}



	public void keyPressed(KeyEvent e) 
	{
		
	}

	public void keyReleased(KeyEvent e) 
	{
		
	}
}

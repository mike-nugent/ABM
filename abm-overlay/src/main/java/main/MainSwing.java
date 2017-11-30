package main;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import areas.InstanceJPanel;
import artifact.AbyssArtifactJPanel;
import artifact.ArtifactData;
import config.ConfigFile;
import config.ConfigJPanel;
import gameinfo.AbilityData;
import gameinfo.IconLoader;
import gameinfo.PlayerData;
import history.QuickHistoryLineScanner;
import history.RecentHistoryParser;
import logreader.AionLogReader;
import tasks.Task;
import xforms.TransformBar;
import xforms.XFormJPanel;

/**
 * Follow instructions to setup jnetpcap here: http://jnetpcap.com/eclipse
 *
 * @author Mike
 *
 */
public class MainSwing
{
    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     *
     * @throws IOException
     */
    public static final int  STAGE_WIDTH  = 900;
    private static final int STAGE_HEIGHT = 900;

    private static JFrame _frame         = new JFrame("Zhule's Tranform Monitor");
    private static JPanel _stage         = new JPanel();
    private static JPanel _loadingScreen = new JPanel();

    private XFormJPanel         _xformStage;
    private ConfigJPanel        _configStage;
    private AbyssArtifactJPanel _artifactStage;
    private JTabbedPane         _tabbedPane;
    private InstanceJPanel      _areaPanel;

    public JFrame getFrame()
    {
        return _frame;
    }

    public static void main(final String[] args)
    {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    createAndShowGUI();
                }
                catch (final IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void createAndShowGUI() throws IOException
    {
        final MainSwing main = new MainSwing();
        main.verifyNoOtherInstancesAreRunning();
        main.buildAndShowUI();
        main.createTrayIcon();
        main.checkDatabase();
        main.startLoggers();
    }

    private void checkDatabase()
    {
        try
        {
            // Database stuff later
            if (true)
            {
                return;
            }

            Class.forName("org.h2.Driver");
            final Connection conn = DriverManager
                    .getConnection("jdbc:h2:file:" + ConfigFile.getOrCreateConfigDir() + "/aion-database", "sa", "");
            System.out.println("Database connection established...");

            final Statement stmt = conn.createStatement();
            stmt.executeUpdate("DROP TABLE table1");
            stmt.executeUpdate("CREATE TABLE table1 ( user varchar(50) )");
            stmt.executeUpdate("INSERT INTO table1 ( user ) VALUES ( 'Claudio' )");
            stmt.executeUpdate("INSERT INTO table1 ( user ) VALUES ( 'Bernasconi' )");

            final ResultSet rs = stmt.executeQuery("SELECT * FROM table1");
            while (rs.next())
            {
                final String name = rs.getString("user");
                System.out.println(name);
            }
            stmt.close();

            conn.close();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            System.out.println("Error in database");
        }
    }

    private void verifyNoOtherInstancesAreRunning()
    {
        if (!isFileshipAlreadyRunning())
        {
            JOptionPane.showMessageDialog(_frame,
                    "Welcome to Zhule's Aion Meter!\n" + "Another instance of this program is already running.\n\n"
                            + "Check the Trey Icons for the currently active instance.",
                    "Program Already Running!", JOptionPane.INFORMATION_MESSAGE, IconLoader.transform);

            System.exit(0);
        }
    }

    private static boolean isFileshipAlreadyRunning()
    {
        try
        {
            final File file = new File("ABM-LOCK.txt");
            final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            final FileLock fileLock = randomAccessFile.getChannel().tryLock();
            if (fileLock != null)
            {
                Runtime.getRuntime().addShutdownHook(new Thread()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            fileLock.release();
                            randomAccessFile.close();
                            file.delete();
                        }
                        catch (final Exception e)
                        {
                            System.out.println("Unable to remove lock file. " + e);
                        }
                    }
                });
                return true;
            }
        }
        catch (final Exception e)
        {
            System.out.println("Unable to create and/or lock file. " + e);
        }
        return false;
    }

    private void createTrayIcon()
    {
        if (!SystemTray.isSupported())
        {
            System.out.println("SystemTray is not supported");
            return;
        }
        _frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(IconLoader.tray_icon.getImage());
        final SystemTray tray = SystemTray.getSystemTray();
        trayIcon.setToolTip("Zhule's Meter");

        // Create a pop-up menu components
        final MenuItem aboutItem = new MenuItem("About");
        final CheckboxMenuItem cb1 = new CheckboxMenuItem("Show Notifications");
        final MenuItem exitItem = new MenuItem("Exit");

        // Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.addSeparator();
        popup.add(exitItem);

        aboutItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                JOptionPane.showMessageDialog(_frame,
                        "About Aion Battle Meter\n" + "Developed by: Zhule - Kahrun Asmodian Cleric\n"
                                + "Version:      1.0\n" + "Information:  http://blindparadox.com/",
                        "Information about ABM", JOptionPane.INFORMATION_MESSAGE, IconLoader.transform);
            }
        });

        exitItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                System.exit(0);
            }
        });

        trayIcon.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(final MouseEvent e)
            {
                _frame.setVisible(true);
                _frame.setState(JFrame.NORMAL);
            }
        });

        trayIcon.setPopupMenu(popup);

        try
        {
            tray.add(trayIcon);
        }
        catch (final AWTException e)
        {
            System.out.println("TrayIcon could not be added.");
        }
    }

    public void startLoggers()
    {
        // Quick verification the setup is correct:
        if (ConfigFile.isSetup())
        {
            _tabbedPane.setEnabledAt(0, true);
            _tabbedPane.setEnabledAt(1, true);
            _tabbedPane.setEnabledAt(2, true);
            _tabbedPane.setEnabledAt(3, true);
            _tabbedPane.setSelectedIndex(1);

            final JLabel title = new JLabel("Loading Recent History, Please Wait A Minute... ");
            title.setFont(new Font(Font.DIALOG, Font.BOLD, 33));

            final JTextArea area = new JTextArea("Lines Read: 0");
            final JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            pane.setViewportView(area);
            area.setAutoscrolls(true);
            area.setBackground(Color.BLACK);
            area.setForeground(Color.yellow);
            area.setEditable(false);
            area.setFont(new Font(Font.DIALOG, Font.BOLD, 10));

            _loadingScreen.setLayout(new BoxLayout(_loadingScreen, BoxLayout.Y_AXIS));
            _loadingScreen.add(Box.createHorizontalGlue());
            _loadingScreen.add(title);
            _loadingScreen.add(pane);
            _loadingScreen.add(Box.createVerticalGlue());

            _frame.getContentPane().remove(_stage);
            _frame.getContentPane().add(_loadingScreen);

            final QuickHistoryLineScanner scanner = new QuickHistoryLineScanner();
            final RecentHistoryParser parser = new RecentHistoryParser();

            final Task task = scanner.scanFile(parser);

            final Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        final List<String> currentLines = new LinkedList<String>();
                        while (!task.isTerminal())
                        {
                            if (currentLines.size() >= 10)
                            {
                                currentLines.remove(0);
                            }

                            currentLines.add(task.getInfoString());

                            String newTxt = "";

                            for (final String line : currentLines)
                            {
                                newTxt += line + "\n";
                            }

                            area.setText(newTxt);

                            Thread.sleep(10);
                        }

                        System.out.println("FINALY : " + task.getTaskState());
                        _frame.getContentPane().remove(_loadingScreen);
                        _frame.getContentPane().add(_stage);
                        _frame.repaint();
                    }
                    catch (final Exception x)
                    {
                        x.printStackTrace();
                    }
                }

            };

            final Thread internalThread = new Thread(r);
            internalThread.start();

            if (!AionLogReader.isRunning())
            {
                AionLogReader.readLog();
            }
        }
        else
        {
            // Is not set up, go through first time setup
            JOptionPane.showMessageDialog(_frame,
                    "Welcome to Zhule's Aion Meter!\n"
                            + "To use this tool, please finish the configuration on the \"Config\" tab",
                    "First Time Setup Required!", JOptionPane.INFORMATION_MESSAGE);

            _tabbedPane.setEnabledAt(0, false);
            _tabbedPane.setEnabledAt(1, false);
            _tabbedPane.setEnabledAt(2, false);
            _tabbedPane.setEnabledAt(3, false);

            _tabbedPane.setSelectedIndex(4);
        }
    }

    private void buildAndShowUI()
    {

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (final Exception e1)
        {
            e1.printStackTrace();
        }

        // Create and set up the window.
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setSize(900, 900);
        _frame.setMinimumSize(new Dimension(STAGE_WIDTH, STAGE_HEIGHT));
        _frame.setPreferredSize(new Dimension(STAGE_WIDTH, STAGE_HEIGHT));

        // Add the stage to the frame (window). The stage is the content area
        // where we will add things to.
        _stage.setLayout(new BoxLayout(_stage, BoxLayout.PAGE_AXIS));

        _frame.getContentPane().add(_stage);

        _xformStage = new XFormJPanel(this);
        _configStage = new ConfigJPanel(this);

        final JPanel artifactStage = new JPanel();
        artifactStage.setLayout(null);
        _artifactStage = new AbyssArtifactJPanel();
        artifactStage.add(_artifactStage);
        artifactStage.setBackground(Color.black);
        _artifactStage.setLocation(40, 0);

        _areaPanel = new InstanceJPanel(this);

        _tabbedPane = new JTabbedPane();
        _tabbedPane.setBackground(Color.gray);
        _tabbedPane.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        _tabbedPane.addTab("Transform Tracker", IconLoader.transform, _xformStage);
        _tabbedPane.addTab("Instances & PvP", IconLoader.atrea, _areaPanel);
        _tabbedPane.addTab("Tools & Data", IconLoader.faction, new JPanel());
        _tabbedPane.addTab("Artifact Timer", IconLoader.artifact, artifactStage);
        _tabbedPane.addTab("Settings", IconLoader.config, _configStage);
        _stage.add(_tabbedPane);

        _tabbedPane.setSelectedIndex(1);

        // Display the window.
        _frame.pack();
        _frame.setLocationRelativeTo(null);
        _frame.setVisible(true);
        // TODO - make this an option: _frame.setAlwaysOnTop(true);

        try
        {
            final Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/transform_icon.jpg"));
            final ImageIcon icon = new ImageIcon(image);
            _frame.setIconImage(icon.getImage());
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }

    }

    public void moveBarToInactive(final TransformBar transformBar)
    {
        System.out.println("Transferring xform from live to dead: " + transformBar.getPlayerName());
        _xformStage.transformDied(transformBar);
        final int newCount = _xformStage.getNumActiveXforms();
        updateXformTab(newCount);
        _stage.repaint();
    }

    public synchronized void transformDetected(final PlayerData data)
    {
        System.out.println("Showing xform now");
        _xformStage.addNewTransform(data);
        final int newCount = _xformStage.getNumActiveXforms();
        updateXformTab(newCount);
        _stage.repaint();
    }

    public synchronized void removeCooldown(final TransformBar transformBar)
    {
        System.out.println("The cooldown is over for: " + transformBar.getPlayerName());
        _xformStage.cooldownFinished(transformBar);
        final int newCount = _xformStage.getNumActiveXforms();
        updateXformTab(newCount);
        _stage.repaint();
    }

    public synchronized void checkDeath(final PlayerData data)
    {
        if (_xformStage.checkDeath(data))
        {
            final int newCount = _xformStage.getNumActiveXforms();
            updateXformTab(newCount);
            _stage.repaint();
        }
    }

    private synchronized void updateXformTab(final int newCount)
    {
        final int count = _tabbedPane.getTabCount();
        for (int i = 0; i < count; i++)
        {
            final String label = _tabbedPane.getTitleAt(i);
            if (label.contains("Transform"))
            {
                String newLabel;
                if (newCount > 0)
                {
                    newLabel = "Transform Tracker ( " + newCount + " )";
                }
                else
                {
                    newLabel = "Transform Tracker";
                }
                _tabbedPane.setTitleAt(i, newLabel);
                return;
            }
        }
    }

    public synchronized void artifactWasCaptured(final ArtifactData info)
    {
        _artifactStage.artifactWasCaptured(info);
        _stage.repaint();
    }

    public void abilityDetected(final AbilityData data)
    {
        _areaPanel.abilityDetected(data);
    }

}

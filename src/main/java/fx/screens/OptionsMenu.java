package fx.screens;

import config.ConfigFile;
import gameinfo.IconLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.ASDMStage;
import main.DisplayManager;
import main.MainFX;

public class OptionsMenu
{
    private static ContextMenu _optionsMenu;

    final static RadioMenuItem smallIcons = new RadioMenuItem("Small Icons");
    final static RadioMenuItem largeIcons = new RadioMenuItem("Large Icons");

    public static void openOptionsMenu()
    {
        if (_optionsMenu == null)
        {
            _optionsMenu = new ContextMenu();
            // menu.setAutoHide(false);
            final MenuItem exit = new MenuItem("Exit ASDM", new ImageView(IconLoader.loadFxImage("close.png", 25)));
            exit.setStyle("-fx-font-weight:bold;");
            exit.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(final ActionEvent t)
                {
                    System.out.println("Exiting program");
                    System.exit(0);
                }
            });
            // ------------------------------------------------------
            final MenuItem settings = new MenuItem("Main Settings",
                    new ImageView(IconLoader.loadFxImage("config.png", 25)));
            settings.setStyle("-fx-font-weight:bold;");
            settings.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(final ActionEvent t)
                {
                    DisplayManager.toggleConfigPopup();
                }
            });

            // --------------------------------------------
            final Menu uiSizes = new Menu("UI Size", new ImageView(IconLoader.loadFxImage("icon-sizes.png", 25)));

            final String uiSize = ConfigFile.getProperty(ConfigFile.UI_SIZES);
            if (uiSize != null)
            {
                if (uiSize.equals("small"))
                {
                    smallIcons.setSelected(true);
                }
                else
                {
                    largeIcons.setSelected(true);
                }
            }
            else
            {
                largeIcons.setSelected(true);
            }

            final ToggleGroup toggleGroup = new ToggleGroup();
            smallIcons.setToggleGroup(toggleGroup);
            largeIcons.setToggleGroup(toggleGroup);

            smallIcons.setOnAction(new EventHandler<ActionEvent>()
            {

                @Override
                public void handle(final ActionEvent event)
                {
                    updateMenuSizeSelection(event);
                }

            });
            largeIcons.setOnAction(new EventHandler<ActionEvent>()
            {

                @Override
                public void handle(final ActionEvent event)
                {
                    updateMenuSizeSelection(event);

                }
            });

            uiSizes.getItems().addAll(largeIcons, smallIcons);

            // ----------------------------------------------------------------
            final CheckMenuItem onTop = new CheckMenuItem("Always on top",
                    new ImageView(IconLoader.loadFxImage("on-top.png", 30)));
            onTop.setSelected(true);
            onTop.selectedProperty().addListener(new ChangeListener<Boolean>()
            {
                @Override
                public void changed(final ObservableValue ov, final Boolean old_val, final Boolean new_val)
                {
                    ASDMStage.getStage().setAlwaysOnTop(new_val);
                }
            });

            final CheckMenuItem lockUI = new CheckMenuItem("Lock UI position",
                    new ImageView(IconLoader.loadFxImage("lock.png", 30)));

            final String isSet = ConfigFile.getProperty(ConfigFile.LOCK_WINDOW_POSITION);
            if (isSet != null && isSet.equals("true"))
            {
                lockUI.setSelected(true);
            }
            else
            {
                lockUI.setSelected(false);
            }

            lockUI.selectedProperty().addListener(new ChangeListener<Boolean>()
            {
                @Override
                public void changed(final ObservableValue ov, final Boolean old_val, final Boolean new_val)
                {
                    // ASDMStage.getStage().setAlwaysOnTop(new_val);
                    ASDMStage.setWindowLock(new_val);
                    ConfigFile.setProperty(ConfigFile.LOCK_WINDOW_POSITION, new_val + "");
                }
            });

            final MenuItem minimize = new MenuItem("Minimize to tray",
                    new ImageView(IconLoader.loadFxImage("minimize.png", 25)));
            minimize.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(final ActionEvent t)
                {
                    System.out.println("TODO - implement minimize this in new framework");
                    ASDMStage.getStage().setIconified(true);
                }
            });

            // don't have to add any listener for this case, clicking the object
            // closes the menu as default behavior
            final MenuItem close = new MenuItem("Collapse This Menu");

            _optionsMenu.getItems().addAll(exit, new SeparatorMenuItem(), settings, new SeparatorMenuItem(), uiSizes,
                    onTop, lockUI, minimize, close);
        }

        if (_optionsMenu.isShowing())
        {
            _optionsMenu.hide();
        }
        else
        {
            // Show stage first, then reposition it after, otherwise the
            // .getWidth() and .getHeight() are set to 0.0
            final Stage stage = ASDMStage.getStage();
            _optionsMenu.show(stage);

            _optionsMenu.setX(stage.getX() + stage.getWidth() - _optionsMenu.getWidth());
            _optionsMenu.setY(stage.getY() - _optionsMenu.getHeight() + 20);

        }
    }

    private static void updateMenuSizeSelection(final ActionEvent event)
    {
        final RadioMenuItem item = (RadioMenuItem) event.getSource();
        if (smallIcons.isSelected())
        {
            MainFX.changeIconSizes(30);
            ConfigFile.setProperty(ConfigFile.UI_SIZES, "small");
        }
        else
        {
            MainFX.changeIconSizes(60);
            ConfigFile.setProperty(ConfigFile.UI_SIZES, "large");
        }
    }
}

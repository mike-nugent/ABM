package fx.screens;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Stage;
import main.ASDMStage;
import main.DisplayManager;

public class AdditionalToolMenu
{
    private static ContextMenu _toolMenu;

    public static void openToolMenu()
    {
        if (_toolMenu == null)
        {
            _toolMenu = new ContextMenu();


            // ----------------------------------------------------------------
            final CheckMenuItem idl = new CheckMenuItem("Idgel Dome Landmark PvP Overlay");
            final CheckMenuItem kbf = new CheckMenuItem("Kamar Battlefield PvP Overlay");
            final CheckMenuItem ow = new CheckMenuItem("Ophidian Warpath PvP Overlay");
            final CheckMenuItem iww = new CheckMenuItem("Iron Wall Warfront PvP Overlay");
            final CheckMenuItem dredg = new CheckMenuItem("Ashunatal Dredgion PvP Overlay");

            idl.setDisable(true);
            kbf.setDisable(true);
            ow.setDisable(true);
            iww.setDisable(true);
            dredg.setDisable(true);
            
            final CheckMenuItem upperAbyss = new CheckMenuItem("Upper Abyss Artifact Timer");
            final CheckMenuItem lowerAbyss = new CheckMenuItem("Lower Abyss Artifact Timer");

            lowerAbyss.setDisable(true);

            upperAbyss.selectedProperty().addListener(new ChangeListener<Boolean>()
            {
                @Override
                public void changed(final ObservableValue ov, final Boolean old_val, final Boolean new_val)
                {
                	DisplayManager.toggleArtifactPopup();
                }
            });

         
            final MenuItem close = new MenuItem("Collapse This Menu");
            _toolMenu.getItems().addAll(idl, kbf, ow, iww, dredg,new SeparatorMenuItem(), upperAbyss, lowerAbyss,new SeparatorMenuItem(), close);
        }

        if (_toolMenu.isShowing())
        {
            _toolMenu.hide();
        }
        else
        {
            // Show stage first, then reposition it after, otherwise the
            // .getWidth() and .getHeight() are set to 0.0
            final Stage stage = ASDMStage.getStage();
            _toolMenu.show(stage);

            _toolMenu.setX(stage.getX());
            _toolMenu.setY(stage.getY() - _toolMenu.getHeight() + 20);

        }
    }

}

package fx.screens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Stage;
import main.ABMStage;
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

            final MenuItem upperAbyss = new MenuItem("Upper Abyss Artifact Timer");
            final MenuItem lowerAbyss = new MenuItem("Lower Abyss Artifact Timer");

            lowerAbyss.setDisable(true);
            upperAbyss.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(final ActionEvent event)
                {
                    DisplayManager.toggleArtifactPopup();
                }
            });

            final MenuItem loot = new MenuItem("Loot History");
            loot.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(final ActionEvent event)
                {
                    DisplayManager.toggleLootPopup();
                }
            });

            final MenuItem dice = new MenuItem("Dice Tool");
            dice.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(final ActionEvent event)
                {
                    DisplayManager.toggleDicePopup();
                }
            });

            final MenuItem close = new MenuItem("Collapse This Menu");
            close.setStyle("-fx-font-weight:bold;");

            _toolMenu.getItems().addAll(idl, kbf, ow, iww, dredg, new SeparatorMenuItem(), upperAbyss, lowerAbyss, new SeparatorMenuItem(), loot, dice, new SeparatorMenuItem(), close);
        }

        if (_toolMenu.isShowing())
        {
            _toolMenu.hide();
        }
        else
        {
            // Show stage first, then reposition it after, otherwise the
            // .getWidth() and .getHeight() are set to 0.0
            final Stage stage = ABMStage.getStage();
            _toolMenu.show(stage);

            _toolMenu.setX(stage.getX());
            _toolMenu.setY(stage.getY() - _toolMenu.getHeight() + 20);

        }
    }

}

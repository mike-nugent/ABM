package fx.screens;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class PlayersPopupPage extends PopupStage
{
    TabPane _tabPane = new TabPane();

    public PlayersPopupPage()
    {
        super("Shugo Database");
        final Tab playersTab = new Tab("Players");
        final Tab itemsTab = new Tab("XP & Items");

        playersTab.setClosable(false);
        itemsTab.setClosable(false);

        playersTab.setStyle("-fx-font-size: 18px; ");
        itemsTab.setStyle("-fx-font-size: 18px; ");

        _tabPane.getTabs().addAll(playersTab, itemsTab);
        _tabPane.setTabMaxHeight(40);

        playersTab.setContent(new SearchPlayersScreen());
        itemsTab.setContent(new SearchXpAndItemsScreen());
        stage.getChildren().add(_tabPane);
    }
}

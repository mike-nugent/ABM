package loot;

import java.util.HashMap;
import java.util.Map;

import fx.screens.PopupStage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LootPopupPage extends PopupStage
{
    public static LootPopupPage me;

    HBox   playerHBox = new HBox(10);
    Button btn        = new Button("Reset");

    Map<String, PlayerLootTable> looters = new HashMap<String, PlayerLootTable>();

    public LootPopupPage()
    {
        super("Loot History");
        me = this;

        btn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {
                playerHBox.getChildren().removeAll(playerHBox.getChildren());
                looters.clear();
            }
        });

        playerHBox.setPadding(new Insets(10));
        stage.getChildren().addAll(new VBox(btn, playerHBox));
    }

    public static void showNewLootRecieved(final String person, final int totalNumItems, final String itemID)
    {
        me.showNewLoot(person, totalNumItems, itemID);
    }

    protected void showNewLoot(final String person, final int totalNumItems, final String itemID)
    {
        // Get or create box for the person
        final PlayerLootTable playerBox = getOrCreatePlayer(person);
        playerBox.addItem(itemID, totalNumItems);
    }

    private PlayerLootTable getOrCreatePlayer(final String person)
    {
        PlayerLootTable plt = looters.get(person);
        if (plt == null)
        {
            plt = new PlayerLootTable(person);
            looters.put(person, plt);
            playerHBox.getChildren().add(plt);
        }

        return plt;
    }

}

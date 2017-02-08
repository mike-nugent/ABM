package loot;

import java.util.HashMap;
import java.util.Map;

import gameinfo.IconLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PlayerLootTable extends VBox
{
    String               playerName;
    Map<String, Integer> quantaties = new HashMap<String, Integer>();
    Map<String, Label>   labels     = new HashMap<String, Label>();
    Label                junkLabel  = new Label("Junk", new ImageView(IconLoader.loadFxImage("junk-icon.png")));

    public PlayerLootTable(final String playerName)
    {
        this.playerName = playerName;
        this.getChildren().add(new Label(playerName));
        labels.put("JUNK", junkLabel);
        quantaties.put("JUNK", 0);
        junkLabel.setTooltip(new Tooltip("JUNK"));
    }

    public String getName()
    {
        return playerName;
    }

    public void addItem(final String itemID, final int amount)
    {
        final ItemData itemData = new ItemData(itemID);
        if (itemData.getItemQuality().equals("JUNK"))
        {
            if (!this.getChildren().contains(junkLabel))
            {
                this.getChildren().add(junkLabel);
            }

            quantaties.put("JUNK", quantaties.get("JUNK") + amount);
            junkLabel.setText("x" + quantaties.get("JUNK"));
            return;
        }

        if (quantaties.containsKey(itemID))
        {
            quantaties.put(itemID, quantaties.get(itemID) + amount);
            updateAmount(itemID);
        }
        else
        {

            final ImageView img = IconLoader.getItemIcon(itemID);
            final Label item = new Label("x" + amount, img);
            item.setTooltip(new Tooltip(itemData.getItemName() + " quality:" + itemData.getItemQuality()));

            quantaties.put(itemID, amount);
            labels.put(itemID, item);

            this.getChildren().add(item);
        }
    }

    private void updateAmount(final String itemID)
    {
        final Label lbl = labels.get(itemID);
        lbl.setText("x" + quantaties.get(itemID));
    }
}

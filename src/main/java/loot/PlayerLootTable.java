package loot;

import java.util.HashMap;
import java.util.Map;

import gameinfo.IconLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PlayerLootTable extends VBox
{
    String               playerName;
    Map<String, Integer> quantaties = new HashMap<String, Integer>();
    Map<String, Label>   labels     = new HashMap<String, Label>();
    Label                junkLabel;

    public PlayerLootTable(final String playerName)
    {
        final ImageView junkIcon = new ImageView(IconLoader.loadFxImage("junk-icon.png"));
        junkIcon.setFitHeight(30);
        junkIcon.setFitWidth(30);
        junkLabel = makeLabel(junkIcon, "Junk", Color.AZURE, 14);
        this.playerName = playerName;
        this.getChildren().add(makeLabel(playerName, Color.AQUAMARINE, 15));
        labels.put("JUNK", junkLabel);
        quantaties.put("JUNK", 0);
        junkLabel.setTooltip(new Tooltip("JUNK"));
    }

    private Label makeLabel(final ImageView image, final String txt, final Color color, final int size)
    {
        final Label label = new Label(txt, image);
        label.setTextFill(color);
        label.setFont(Font.font(null, FontWeight.BOLD, size));
        return label;
    }

    private Label makeLabel(final String txt, final Color color, final int size)
    {
        final Label label = new Label(txt);
        label.setTextFill(color);
        label.setFont(Font.font(null, FontWeight.BOLD, size));
        return label;
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
            if (quantaties.get("JUNK") > 1)
            {
                junkLabel.setText("x" + quantaties.get("JUNK"));
            }
            else
            {
                junkLabel.setText("");
            }
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
            img.setFitHeight(30);
            img.setFitWidth(30);
            final Label item = makeLabel(img, "x" + amount, Color.AZURE, 14);
            // item.setTooltip(new Tooltip(itemData.getItemName() + " quality:" + itemData.getItemQuality()));
            System.out.println(itemData.getJson());
            item.setTooltip(new Tooltip(itemData.getJson()));
            if (amount == 1)
            {
                item.setText("");
            }

            quantaties.put(itemID, amount);
            labels.put(itemID, item);

            this.getChildren().add(item);
        }
    }

    private void updateAmount(final String itemID)
    {
        final Label lbl = labels.get(itemID);
        final int amount = quantaties.get(itemID);
        if (amount > 1)
        {
            lbl.setText("x" + quantaties.get(itemID));
        }
        else
        {
            lbl.setText("");
        }
    }
}

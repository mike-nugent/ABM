package loot;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ItemLabel extends Label 
{
	private ItemQuality _quality;
	private ItemData itemData; 
	
	public ItemLabel(String text, Node graphic, final Color color, final int size)
	{
		super(text, graphic);
        this.setTextFill(color);
        this.setFont(Font.font(null, FontWeight.BOLD, size));
	}
	
	public void setItemQuality(ItemQuality quality)
	{
		_quality = quality;
	}
	
	public ItemQuality getItemQuality()
	{
		return _quality;
	}

	public void setItemData(ItemData itemData) 
	{
		//TODO - use this in future;
		this.itemData = itemData;
	}
}

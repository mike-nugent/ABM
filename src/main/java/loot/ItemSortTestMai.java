package loot;

public class ItemSortTestMai 
{
	public static void main(String[] args) 
	{
		ItemSorter sorter = new ItemSorter();
		sorter.addItem(ItemQuality.EPIC);
		sorter.addItem(ItemQuality.MYTHIC);
		sorter.addItem(ItemQuality.COMMON);
		sorter.addItem(ItemQuality.EPIC);
		sorter.addItem(ItemQuality.JUNK);
		sorter.addItem(ItemQuality.JUNK);
		sorter.addItem(ItemQuality.EPIC);
		sorter.addItem(ItemQuality.LEGEND);
		sorter.addItem(ItemQuality.EPIC);
		sorter.addItem(ItemQuality.JUNK);
		sorter.addItem(ItemQuality.RARE);
		sorter.addItem(ItemQuality.MYTHIC);
		
		sorter.printOrder();
	}
}

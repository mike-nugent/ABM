package loot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ItemSorter 
{
	public List<ItemQuality> list = new ArrayList<ItemQuality>();
	
	public ItemSorter()
	{
		
	}
	
	public void addItem(ItemQuality item)
	{
		if(list.size() == 0)
		{
			list.add(item);
		}
		else
		{
			sortInto(list, item);
		}
	}

	private void sortInto(List<ItemQuality> list, ItemQuality item)
	{	
		for(int i = 0; i < list.size(); i++)
		{
			if(item.getIndex() <= list.get(i).getIndex())
			{
				list.add(i, item);
				return;
			}
		}
		
		list.add(item);
		
	}

	public void printOrder() 
	{
		System.out.println(Arrays.toString(list.toArray()));
	}
}

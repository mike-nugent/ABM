package loot;

import java.util.HashMap;

public class LootManager
{
    // super(gave_up, rolled_dice, rolled_highest, everyone_pass, has_acquired);

    static String                   lastPersonToRoll = null;
    static HashMap<String, Integer> rollMap          = new HashMap<String, Integer>();

    public static void playerGaveUpRoll(final String person)
    {
        rollMap.put(person, 0);
        DicePopupPage.showPlayerRoll(person, 0);
    }

    public static void playerRolledDice(final String person, final int roll, final int limit)
    {
        rollMap.put(person, roll);
        DicePopupPage.showPlayerRoll(person, roll);
    }

    public static void playerRolledHighest(final String person)
    {
        int highest = 0;
        for (final int next : rollMap.values())
        {
            if (highest < next)
            {
                highest = next;
            }
        }
        DicePopupPage.updateWinsRoll(person, highest);
        rollMap.clear();
    }

    public static void everyonePassed()
    {
        rollMap.clear();
    }

    public static void playerHasAcquiredItem(final String person, final int totalNumItems, final String itemID)
    {
        LootPopupPage.showNewLootRecieved(person, totalNumItems, itemID);
    }
}

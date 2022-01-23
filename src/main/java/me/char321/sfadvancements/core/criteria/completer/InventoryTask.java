package me.char321.sfadvancements.core.criteria.completer;

import org.bukkit.scheduler.BukkitRunnable;

public class InventoryTask extends BukkitRunnable {
    /*
    private final Map<ItemStack, Advancement> goals = new HashMap<>();

    public InventoryTask() {
        reload();
    }
    */

    @Override
    public void run() {
        /*
        for(Player p : Bukkit.getOnlinePlayers()) {
            Inventory inv = p.getInventory();
            Map<ItemStack, Integer> contents = new HashMap<>();
            for (ItemStack item : inv) {
                if(item == null || item.getType() == Material.AIR) {
                    continue;
                }
                ItemStack clone = item.clone();
                clone.setAmount(1);
                contents.merge(clone, item.getAmount(), Integer::sum);
            }
            for (ItemStack item : contents.keySet()) {
                for (ItemStack goalitem : goals.keySet()) {
                    if(SlimefunUtils.isItemSimilar(item, goalitem, true)) {
                        goals.get(goalitem).complete(p);
                    }
                }
            }
        }
         */
    }
    /*
    public void reload() {
        for (Advancement adv : SFAdvancements.getRegistry().getAdvancements().values()) {
            if(adv instanceof InventoryAdvancement) {
                InventoryAdvancement invadv = (InventoryAdvancement) adv;
                ItemStack item = invadv.getGoal();
                goals.put(item, adv);
            }
        }
    }
     */
}

package me.char321.sfadvancements.core.command.tasks;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.api.InventoryAdvancement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class InventoryTask extends BukkitRunnable {
    private final Map<ItemStack, Advancement> goals = new HashMap<>();

    public InventoryTask() {
        reload();
    }

    @Override
    public void run() {
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
                if(goals.containsKey(item)) {
                    goals.get(item).complete(p);
                }
            }
        }
    }

    public void reload() {
        for (Advancement adv : SFAdvancements.getRegistry().getAdvancements().values()) {
            if(adv instanceof InventoryAdvancement) {
                InventoryAdvancement invadv = (InventoryAdvancement) adv;
                ItemStack item = invadv.getGoal();
                goals.put(item, adv);
            }
        }
    }

}

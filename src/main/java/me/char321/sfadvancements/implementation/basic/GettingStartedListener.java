package me.char321.sfadvancements.implementation.basic;

import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockInteractEvent;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.EnhancedCraftingTable;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class GettingStartedListener implements Listener {

    public GettingStartedListener() {
        Bukkit.getPluginManager().registerEvents(this, SFAdvancements.instance());
    }

    @EventHandler
    public void onCraft(MultiBlockInteractEvent e) {
        if(e.getMultiBlock().getSlimefunItem() instanceof EnhancedCraftingTable) {
            Utils.fromKey("gettingstarted").complete(e.getPlayer());
        }
    }

}

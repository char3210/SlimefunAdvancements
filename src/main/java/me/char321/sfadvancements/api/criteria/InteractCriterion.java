package me.char321.sfadvancements.api.criteria;

import com.google.gson.JsonObject;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.core.AdvManager;
import me.char321.sfadvancements.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * impractical, only for example purposes
 */
public class InteractCriterion extends CountCriterion implements Listener {
    private final ItemStack item;

    public InteractCriterion(NamespacedKey adv, int amount, ItemStack item) {
        super(adv, amount);
        this.item = item;

        Bukkit.getPluginManager().registerEvents(this, SFAdvancements.instance());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent e) {
        if(!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if(!SlimefunUtils.isItemSimilar(e.getItem(), item, false)) {
            return;
        }

        UUID uuid = e.getPlayer().getUniqueId();
        increment(uuid);
//        SFAdvancements.getAdvManager().completeCriterion(uuid, this);
    }

    @Override
    public JsonObject save(Player p) {
        return null;
    }
}

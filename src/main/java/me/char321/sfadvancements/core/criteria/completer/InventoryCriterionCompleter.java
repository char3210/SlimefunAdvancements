package me.char321.sfadvancements.core.criteria.completer;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.api.criteria.InventoryCriterion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InventoryCriterionCompleter implements CriterionCompleter, Listener {
    private final Map<Material, Set<InventoryCriterion>> criteria = new EnumMap<>(Material.class);

    public InventoryCriterionCompleter() {
        Bukkit.getPluginManager().registerEvents(this, SFAdvancements.instance());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventory(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            Material material = e.getItem().getItemStack().getType();
            if (!criteria.containsKey(material)) {
                return;
            }
            for (InventoryCriterion criterion : criteria.get(material)) {
                if (SlimefunUtils.isItemSimilar(criterion.getItem(), e.getItem().getItemStack(), false, false)) {
                    criterion.perform(p);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent e) {
        onInventory1((Player)e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent e) {
        onInventory1((Player)e.getPlayer());
    }

    public void onInventory1(Player p) {
        Inventory inv = p.getInventory();
        Map<ItemStack, Integer> contents = new HashMap<>();
        for (ItemStack item : inv) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }
            ItemStack clone = item.clone();
            clone.setAmount(1);
            contents.merge(clone, item.getAmount(), Integer::sum);
        }
        for (ItemStack item : contents.keySet()) {
            Material material = item.getType();
            if (!criteria.containsKey(material)) {
                continue;
            }
            for (InventoryCriterion criterion : criteria.get(material)) {
                if (SlimefunUtils.isItemSimilar(criterion.getItem(), item, false, false)) {
                    criterion.perform(p);
                }
            }
        }
    }

    @Override
    public void register(Criterion criterion) {
        if (!(criterion instanceof InventoryCriterion)) {
            throw new IllegalArgumentException("criterion must be an interactcriterion");
        }
        InventoryCriterion criterion1 = (InventoryCriterion) criterion;
        Material m = criterion1.getItem().getType();
        criteria.computeIfAbsent(m, k -> new HashSet<>());
        criteria.get(m).add(criterion1);
    }

    @Override
    public Class<? extends Criterion> getCriterionClass() {
        return InventoryCriterion.class;
    }

    @Override
    public void reload() {
        criteria.clear();
    }
}

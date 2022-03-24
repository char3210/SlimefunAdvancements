package me.char321.sfadvancements.core.criteria.completer;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.ConsumeCriterion;
import me.char321.sfadvancements.api.criteria.Criterion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConsumeCriterionCompleter implements CriterionCompleter, Listener {
    private final Map<Material, Set<ConsumeCriterion>> criteria = new EnumMap<>(Material.class);

    public ConsumeCriterionCompleter() {
        Bukkit.getPluginManager().registerEvents(this, SFAdvancements.instance());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onConsume(PlayerItemConsumeEvent e) {
        ItemStack consumed = e.getItem();
        Set<ConsumeCriterion> allCriteria = criteria.get(consumed.getType());
        if (allCriteria == null) {
            return;
        }

        for (ConsumeCriterion criterion : allCriteria) {
            if (SlimefunUtils.isItemSimilar(consumed, criterion.getItem(), false, false)) {
                criterion.perform(e.getPlayer());
            }
        }
    }

    @Override
    public void register(Criterion criterion) {
        if (!(criterion instanceof ConsumeCriterion)) {
            throw new IllegalArgumentException("criterion must be an ConsumeCriterion");
        }
        ConsumeCriterion criterion1 = (ConsumeCriterion) criterion;
        Material m = criterion1.getItem().getType();
        criteria.computeIfAbsent(m, k -> new HashSet<>());
        criteria.get(m).add(criterion1);
    }

    @Override
    public Class<? extends Criterion> getCriterionClass() {
        return ConsumeCriterion.class;
    }

    @Override
    public void reload() {
        criteria.clear();
    }
}

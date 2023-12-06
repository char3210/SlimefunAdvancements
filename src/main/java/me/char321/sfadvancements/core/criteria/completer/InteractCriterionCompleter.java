package me.char321.sfadvancements.core.criteria.completer;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.api.criteria.InteractCriterion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InteractCriterionCompleter implements Listener, CriterionCompleter {
    private final Map<Material, Set<InteractCriterion>> criteria = new EnumMap<>(Material.class);

    public InteractCriterionCompleter() {
        Bukkit.getPluginManager().registerEvents(this, SFAdvancements.instance());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerRightClickEvent e) {
        ItemStack clicked = e.getItem();
        Set<InteractCriterion> allCriteria = criteria.get(clicked.getType());
        if (allCriteria == null) {
            return;
        }

        for (InteractCriterion criterion : allCriteria) {
            if (SlimefunUtils.isItemSimilar(clicked, criterion.getItem(), false, false)) {
                criterion.perform(e.getPlayer());
            }
        }
    }

    @Override
    public void register(Criterion criterion) {
        if (!(getCriterionClass().isInstance(criterion))) {
            throw new IllegalArgumentException("criterion must be an " + getCriterionClass().getName());
        }

        InteractCriterion criterion1 = (InteractCriterion) criterion;
        Material m = criterion1.getItem().getType();
        criteria.computeIfAbsent(m, k -> new HashSet<>()).add(criterion1);
    }

    @Override
    public Class<? extends Criterion> getCriterionClass() {
        return InteractCriterion.class;
    }

    @Override
    public void reload() {
        criteria.clear();
    }
}

package me.char321.sfadvancements.core.criteria.completer;

import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockInteractEvent;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.api.criteria.MultiBlockCriterion;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MultiBlockCriterionCompleter implements CriterionCompleter, Listener {
    private final Map<String, Set<MultiBlockCriterion>> criteria = new HashMap<>();

    public MultiBlockCriterionCompleter() {
        Bukkit.getPluginManager().registerEvents(this, SFAdvancements.instance());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMultiBlock(MultiBlockInteractEvent e) {
        String machineid = e.getMultiBlock().getSlimefunItem().getId();
        if(!criteria.containsKey(machineid)) {
            return;
        }
        for (MultiBlockCriterion criterion : criteria.get(machineid)) {
            criterion.perform(e.getPlayer());
        }
    }

    @Override
    public void register(Criterion criterion) {
        if (!(criterion instanceof MultiBlockCriterion)) {
            throw new IllegalArgumentException("criterion must be an multiblockcriterion");
        }
        MultiBlockCriterion criterion1 = (MultiBlockCriterion) criterion;
        String machine = criterion1.getMachineId();
        criteria.computeIfAbsent(machine, k -> new HashSet<>());
        criteria.get(machine).add(criterion1);
    }

    @Override
    public Class<? extends Criterion> getCriterionClass() {
        return MultiBlockCriterion.class;
    }

    @Override
    public void reload() {
        criteria.clear();
    }
}

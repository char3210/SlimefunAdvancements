package me.char321.sfadvancements.core.criteria.completer;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.ConsumeCriterion;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.api.criteria.MobKillCriterion;
import me.char321.sfadvancements.api.criteria.PlaceCriterion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MobKillCriterionCompleter implements CriterionCompleter, Listener {
    private final List<MobKillCriterion> criteria = new ArrayList<>();

    public MobKillCriterionCompleter() {
        Bukkit.getPluginManager().registerEvents(this, SFAdvancements.instance());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) {
            return;
        }

        for (MobKillCriterion criterion : criteria) {
            String name = criterion.getEntity();
            if (e.getEntityType().equals(EntityType.fromName(name))) {
                criterion.perform(killer);
            }
        }
    }

    @Override
    public void register(Criterion criterion) {
        if (!(criterion instanceof MobKillCriterion)) {
            throw new IllegalArgumentException("criterion must be an ConsumeCriterion");
        }
        MobKillCriterion criterion1 = (MobKillCriterion) criterion;
        criteria.add(criterion1);
    }

    @Override
    public Class<? extends Criterion> getCriterionClass() {
        return MobKillCriterion.class;
    }

    @Override
    public void reload() {
        criteria.clear();
    }
}

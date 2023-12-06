package me.char321.sfadvancements.core.criteria.completer;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.api.criteria.MobKillCriterion;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MobKillCriterionCompleter implements CriterionCompleter, Listener {
    private final Map<EntityType, List<MobKillCriterion>> criteria = new EnumMap<>(EntityType.class);

    public MobKillCriterionCompleter() {
        Bukkit.getPluginManager().registerEvents(this, SFAdvancements.instance());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) {
            return;
        }

        EntityType entityType = e.getEntityType();
        List<MobKillCriterion> mobKillCriteria = criteria.get(entityType);
        if (mobKillCriteria == null) {
            return;
        }
        for (MobKillCriterion criterion : mobKillCriteria) {
            EntityType type = criterion.getEntity();
            if (entityType.equals(type)) {
                criterion.perform(killer);
            }
        }
    }

    @Override
    public void register(Criterion criterion) {
        if (!(getCriterionClass().isInstance(criterion))) {
            throw new IllegalArgumentException("criterion must be an " + getCriterionClass().getName());
        }

        MobKillCriterion criterion1 = (MobKillCriterion) criterion;
        criteria.computeIfAbsent(criterion1.getEntity(), k -> new ArrayList<>()).add(criterion1);
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

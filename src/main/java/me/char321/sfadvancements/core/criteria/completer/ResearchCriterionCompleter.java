package me.char321.sfadvancements.core.criteria.completer;

import io.github.thebusybiscuit.slimefun4.api.events.ResearchUnlockEvent;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.api.criteria.ResearchCriterion;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResearchCriterionCompleter implements CriterionCompleter, Listener {
    private final Map<NamespacedKey, Set<ResearchCriterion>> criteria = new HashMap<>();

    public ResearchCriterionCompleter() {
        Bukkit.getPluginManager().registerEvents(this, SFAdvancements.instance());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PlayerProfile.get(p, (profile) -> {
            for (Research research : profile.getResearches()) {
                performCriteria(p, research);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerResearch(ResearchUnlockEvent e) {
        performCriteria(e.getPlayer(), e.getResearch());
    }

    private void performCriteria(Player player, Research research) {
        Set<ResearchCriterion> allcriteria = criteria.get(research.getKey());
        if (allcriteria == null) {
            return;
        }

        for (ResearchCriterion criterion : allcriteria) {
            criterion.perform(player);
        }
    }

    @Override
    public void register(Criterion criterion) {
        if (!(criterion instanceof ResearchCriterion)) {
            throw new IllegalArgumentException("criterion must be an " + getCriterionClass().getName());
        }

        ResearchCriterion criterion1 = ((ResearchCriterion) criterion);
        NamespacedKey research = criterion1.getResearch();
        criteria.computeIfAbsent(research, k -> new HashSet<>());
        criteria.get(research).add(criterion1);
    }

    @Override
    public Class<? extends Criterion> getCriterionClass() {
        return ResearchCriterion.class;
    }

    @Override
    public void reload() {
        criteria.clear();
    }
}

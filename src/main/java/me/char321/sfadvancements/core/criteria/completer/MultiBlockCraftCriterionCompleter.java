package me.char321.sfadvancements.core.criteria.completer;

import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun4.api.events.ResearchUnlockEvent;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.api.criteria.MultiBlockCraftCriterion;
import me.char321.sfadvancements.api.criteria.ResearchCriterion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MultiBlockCraftCriterionCompleter implements CriterionCompleter, Listener {
    private final Map<Material, Set<MultiBlockCraftCriterion>> criteria = new EnumMap<>(Material.class);

    public MultiBlockCraftCriterionCompleter() {
        Bukkit.getPluginManager().registerEvents(this, SFAdvancements.instance());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMultiBlockCraft(MultiBlockCraftEvent e) {
        performCriteria(e.getPlayer(), e.getOutput());
    }

    private void performCriteria(Player player, ItemStack output) {
        Set<MultiBlockCraftCriterion> allcriteria = criteria.get(output.getType());
        if (allcriteria == null) {
            return;
        }

        for (MultiBlockCraftCriterion criterion : allcriteria) {
            if (SlimefunUtils.isItemSimilar(output, criterion.getItem(), false, false)) {
                criterion.perform(player);
            }
        }
    }

    @Override
    public void register(Criterion criterion) {
        if (!(getCriterionClass().isInstance(criterion))) {
            throw new IllegalArgumentException("criterion must be an " + getCriterionClass().getName());
        }

        MultiBlockCraftCriterion criterion1 = ((MultiBlockCraftCriterion) criterion);
        Material material = criterion1.getItem().getType();
        criteria.computeIfAbsent(material, k -> new HashSet<>()).add(criterion1);
    }

    @Override
    public Class<? extends Criterion> getCriterionClass() {
        return MultiBlockCraftCriterion.class;
    }

    @Override
    public void reload() {
        criteria.clear();
    }
}

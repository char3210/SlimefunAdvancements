package me.char321.sfadvancements.core.criteria.completer;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.char321.sfadvancements.api.criteria.BlockBreakCriterion;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.util.Utils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class BlockBreakCriterionCompleter implements CriterionCompleter, Listener {
    EnumMap<Material, List<BlockBreakCriterion>> criteria = new EnumMap<>(Material.class);

    public BlockBreakCriterionCompleter() {
        Utils.listen(this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        List<BlockBreakCriterion> criteria1 = criteria.get(e.getBlock().getType());
        if (criteria1 == null) {
            return;
        }
        for (BlockBreakCriterion criterion : criteria1) {
            SlimefunItem sfitem = SlimefunItem.getByItem(criterion.getItem());
            if (sfitem == null) {
                criterion.perform(e.getPlayer());
            } else if (sfitem.equals(BlockStorage.check(e.getBlock()))) {
                criterion.perform(e.getPlayer());
            }
        }
    }

    @Override
    public void register(Criterion criterion) {
        if (!(getCriterionClass().isInstance(criterion))) {
            throw new IllegalArgumentException("criterion must be an " + getCriterionClass().getName());
        }

        BlockBreakCriterion criterion1 = (BlockBreakCriterion) criterion;
        criteria.computeIfAbsent(criterion1.getItem().getType(), k -> new ArrayList<>()).add(criterion1);
    }

    @Override
    public Class<? extends Criterion> getCriterionClass() {
        return BlockBreakCriterion.class;
    }

    @Override
    public void reload() {
        criteria.clear();
    }
}

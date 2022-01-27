package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.inventory.ItemStack;

public class InventoryCriterion extends Criterion {
    private final ItemStack item;

    public InventoryCriterion(String id, ItemStack item) {
        super(id);
        this.item = item;

        SFAdvancements.getRegistry().getCompleter(this).register(this);
    }

    public ItemStack getItem() {
        return item;
    }

}

package me.char321.sfadvancements.api.criteria;

import org.bukkit.inventory.ItemStack;

public class InventoryCriterion extends Criterion {
    private final ItemStack item;

    public InventoryCriterion(String id, ItemStack item) {
        super(id);
        item = item.clone();
        item.setAmount(1);
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

}

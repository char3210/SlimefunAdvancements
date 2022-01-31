package me.char321.sfadvancements.api.criteria;

import org.bukkit.inventory.ItemStack;

public class ConsumeCriterion extends Criterion {
    private final ItemStack item;

    public ConsumeCriterion(String id, int amount, String name, ItemStack item) {
        super(id, amount, name);
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}

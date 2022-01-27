package me.char321.sfadvancements.api.criteria;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlaceCriterion extends Criterion {
    private final ItemStack item;

    public PlaceCriterion(String id, int amount, Material item) {
        this(id, amount, new ItemStack(item));
    }

    public PlaceCriterion(String id, int amount, ItemStack item) {
        super(id, amount);
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}

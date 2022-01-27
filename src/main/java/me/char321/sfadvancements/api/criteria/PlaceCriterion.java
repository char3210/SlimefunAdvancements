package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class PlaceCriterion extends Criterion {
    private final ItemStack item;

    public PlaceCriterion(String id, int amount, Material item) {
        this(id, amount, new ItemStack(item));
    }

    public PlaceCriterion(String id, int amount, ItemStack item) {
        super(id, amount);
        this.item = item;

        SFAdvancements.getRegistry().getCompleter(this).register(this);
    }

    public ItemStack getItem() {
        return item;
    }
}

package me.char321.sfadvancements.api;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class InventoryAdvancement extends Advancement {
    private ItemStack goal;

    public InventoryAdvancement(NamespacedKey key, AdvancementGroup group, ItemStack display, String name, ItemStack goal) {
        super(key, group, display, name);
        goal = goal.clone();
        goal.setAmount(1);
        this.goal = goal;
    }

    public ItemStack getGoal() {
        return goal;
    }
}

package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class InventoryCriterion extends Criterion {
    private final ItemStack item;

    public InventoryCriterion(NamespacedKey adv, String id, ItemStack item) {
        super(adv, id);
        this.item = item;

        SFAdvancements.getRegistry().getCompleter(this).register(this);
    }

    public ItemStack getItem() {
        return item;
    }

}

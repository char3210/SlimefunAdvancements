package me.char321.sfadvancements.api.criteria;

import org.bukkit.inventory.ItemStack;

/**
 * impractical, only for example purposes
 */
public class InteractCriterion extends Criterion {
    private final ItemStack item;

    /**
     * this criterion is iterated when a player right clicks an item
     *
     * @param id the id of the criteria (should not be the same as other criteria in the same advancement)
     * @param amount how many times the player has to click
     * @param item the item player clicks
     */
    public InteractCriterion(String id, int amount, ItemStack item) {
        super(id, amount);
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}

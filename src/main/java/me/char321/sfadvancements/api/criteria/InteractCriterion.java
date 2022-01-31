package me.char321.sfadvancements.api.criteria;

import org.bukkit.inventory.ItemStack;

/**
 * this criterion is performed whenever someone interacts with a specified {@link ItemStack}
 */
public class InteractCriterion extends Criterion {
    private final ItemStack item;

    /**
     * creates a criterion when a player right clicks an item
     *
     * @param id the id of the criteria (should not be the same as other criteria in the same advancement)
     * @param amount how many times the player has to click
     * @param item the item player clicks
     */
    public InteractCriterion(String id, int amount, String name, ItemStack item) {
        super(id, amount, name);
        this.item = item;
    }

    /**
     * gets the item that must be clicked to perform the criterion
     *
     * @return the itemstack to be clicked
     */
    public ItemStack getItem() {
        return item;
    }
}

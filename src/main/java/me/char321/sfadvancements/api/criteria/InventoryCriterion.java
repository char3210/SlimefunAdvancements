package me.char321.sfadvancements.api.criteria;

import org.bukkit.inventory.ItemStack;

/**
 * this criterion is performed when someone obtains an item
 * count is not applicable in this class (support for multiple items soon[tm]) //TODO
 */
public class InventoryCriterion extends Criterion {
    private final ItemStack item;

    /**
     * creates an inventory criterion
     *
     * @param id the id of the criterion (should be unique per advancement)
     * @param item the item a player must have to complete this criterion
     */
    public InventoryCriterion(String id, ItemStack item) {
        super(id);
        item = item.clone();
        item.setAmount(1);
        this.item = item;
    }

    /**
     * gets the item needed in a player's inventory to complete the criterion
     *
     * @return the item needed
     */
    public ItemStack getItem() {
        return item;
    }

}

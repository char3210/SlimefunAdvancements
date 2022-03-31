package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.util.ConfigUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * this criterion is performed when someone obtains an item
 * count is not applicable in this class
 */
public class InventoryCriterion extends Criterion {
    private final ItemStack item;

    private final int amount;

    public static InventoryCriterion loadFromConfig(ConfigurationSection config) {
        String id = config.getName();

        String name = config.getString("name");
        if(name == null) {
            name = id;
        }

        ItemStack item = ConfigUtils.getItem(config, "item");
        if (item == null) {
            SFAdvancements.warn("unknown item for inventory criterion " + id);
            return null;
        }

        int amount = config.getInt("amount");
        if (amount <= 0) {
            amount = 1;
        }

        return new InventoryCriterion(id, name, item, amount);
    }

    /**
     * creates an inventory criterion
     *
     * @param id the id of the criterion (should be unique per advancement)
     * @param item the item a player must have to complete this criterion
     */
    public InventoryCriterion(String id, String name, ItemStack item, int amount) {
        super(id, name);
        item = item.clone();
        item.setAmount(1);
        this.item = item;
        this.amount = amount;
    }

    /**
     * gets the item needed in a player's inventory to complete the criterion
     *
     * @return the item needed
     */
    public ItemStack getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }
}

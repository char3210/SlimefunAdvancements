package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.util.ConfigUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * what? an actually practical criterion? no way
 * this criterion is completed whenever someone places a specified item a specified number of times
 */
public class PlaceCriterion extends Criterion {
    private final ItemStack item;

    public static PlaceCriterion loadFromConfig(ConfigurationSection config) {
        String id = config.getName();

        int amount = config.getInt("amount");
        if (amount == 0) {
            amount = 1;
        }

        String name = config.getString("name");
        if(name == null) {
            name = id;
        }

        ItemStack item = ConfigUtils.getItem(config, "item");
        if (item == null) {
            SFAdvancements.warn("unknown item for place criterion " + id);
            return null;
        }

        return new PlaceCriterion(id, amount, name, item);
    }

    /**
     * creates a placecriterion
     *
     * @param id the id of the criterion (should be unique per advancement)
     * @param amount the amount of times someone needs to place the item
     * @param item the material of the item that players must place to perform the criterion (vanilla only)
     */
    public PlaceCriterion(String id, int amount, String name, Material item) {
        this(id, amount, name, new ItemStack(item));
    }

    /**
     * creates a placecriterion
     *
     * @param id the id of the criterion (should be unique per advancement)
     * @param amount the amount of times someone needs to place the item
     * @param item the item that players must place to perform the criterion
     */
    public PlaceCriterion(String id, int amount, String name, ItemStack item) {
        super(id, amount, name);
        this.item = item;
    }

    /**
     * returns the item that players must place to complete this criterion
     *
     * @return the item that players must place to complete this criterion
     */
    public ItemStack getItem() {
        return item;
    }
}

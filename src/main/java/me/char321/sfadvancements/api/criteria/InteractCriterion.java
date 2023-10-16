package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.util.ConfigUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * this criterion is performed whenever someone interacts with a specified {@link ItemStack}
 */
public class InteractCriterion extends Criterion {
    private final ItemStack item;

    public static InteractCriterion loadFromConfig(ConfigurationSection config) {
        String id = config.getName();

        int amount = config.getInt("amount");
        if (amount == 0) {
            amount = 1;
        }

        String name = config.getString("name");
        if(name == null) {
            name = id;
        }

        name = ChatColor.translateAlternateColorCodes('&', name);

        ItemStack item = ConfigUtils.getItem(config, "item");
        if (item == null) {
            SFAdvancements.warn("unknown item for interact criterion " + id);
            return null;
        }

        return new InteractCriterion(id, amount, name, item);
    }

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

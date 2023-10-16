package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.util.ConfigUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * this criterion is performed when a player consumes an item
 *
 */
public class ConsumeCriterion extends Criterion {
    private final ItemStack item;

    public static ConsumeCriterion loadFromConfig(ConfigurationSection config) {
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
            SFAdvancements.warn("unknown item for consume criterion " + id);
            return null;
        }

        return new ConsumeCriterion(id, amount, name, item);
    }

    /**
     * creates a consume criterion
     *
     * @param id the id of the criterion (should be unique per advancement)
     * @param amount the number of items to consume
     * @param name the name of the criterion
     * @param item the item for the player to consume
     */
    public ConsumeCriterion(String id, int amount, String name, ItemStack item) {
        super(id, amount, name);
        this.item = item;
    }

    /**
     * gets the item players should consume
     *
     * @return the item for players to consume
     */
    public ItemStack getItem() {
        return item;
    }
}

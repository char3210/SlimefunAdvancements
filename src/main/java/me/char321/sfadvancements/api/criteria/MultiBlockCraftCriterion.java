package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.util.ConfigUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class MultiBlockCraftCriterion extends Criterion {
    private final ItemStack item;

    public static MultiBlockCraftCriterion loadFromConfig(ConfigurationSection config) {
        String id = config.getName();

        if (!SFAdvancements.instance().isMultiBlockCraftEvent()) {
            SFAdvancements.warn("Multiblock craft events are not available on this version of Slimefun! for criterion " + id);
            return null;
        }

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
            SFAdvancements.warn("unknown item for multiblock craft criterion " + id);
            return null;
        }

        return new MultiBlockCraftCriterion(id, amount, name, item);
    }

    /**
     *
     * @param id
     * @param count
     * @param name
     * @param item the output item of the multiblock craft
     */
    public MultiBlockCraftCriterion(String id, int count, String name, ItemStack item) {
        super(id, count, name);
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}

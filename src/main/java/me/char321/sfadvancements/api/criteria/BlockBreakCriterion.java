package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.util.ConfigUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class BlockBreakCriterion extends Criterion {
    private ItemStack item;

    public static BlockBreakCriterion loadFromConfig(ConfigurationSection config) {
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
            SFAdvancements.warn("unknown item for break criterion " + id);
            return null;
        }

        return new BlockBreakCriterion(id, amount, name, item);

    }

    public BlockBreakCriterion(String id, int count, String name, ItemStack item) {
        super(id, count, name);
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}

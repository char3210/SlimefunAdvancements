package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.util.ConfigUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class MultiBlockCraftCriterion extends Criterion {
    @Nullable private final ItemStack item;
    @Nullable private final String machineId;

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


        ItemStack item = null;
        if (config.contains("item", true)) {
            item = ConfigUtils.getItem(config, "item");
            if (item == null) {
                SFAdvancements.warn("unknown item for multiblock craft criterion " + id);
                return null;
            }
        }

        String machineid = config.getString("multiblock");

        return new MultiBlockCraftCriterion(id, amount, name, item, machineid);
    }

    /**
     *
     * @param item the output item of the multiblock craft.
     * @param machineId multiblock machine id
     */
    public MultiBlockCraftCriterion(String id, int count, String name, @Nullable ItemStack item, @Nullable String machineId) {
        super(id, count, name);
        this.item = item;
        this.machineId = machineId;
    }

    @Nullable
    public ItemStack getItem() {
        return item;
    }

    @Nullable
    public String getMachineId() {
        return machineId;
    }
}

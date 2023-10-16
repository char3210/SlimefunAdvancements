package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class MultiBlockCriterion extends Criterion {
    private final String machineid;

    public static MultiBlockCriterion loadFromConfig(ConfigurationSection config) {
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

        String multiBlockId = config.getString("multiblock");
        if(multiBlockId == null) {
            SFAdvancements.warn("specify a multiblock for criterion " + id);
            return null;
        }

        return new MultiBlockCriterion(id, amount, name, multiBlockId);
    }

    public MultiBlockCriterion(String id, int count, String name, String machineid) {
        super(id, count, name);
        this.machineid = machineid;
    }

    public String getMachineId() {
        return machineid;
    }
}

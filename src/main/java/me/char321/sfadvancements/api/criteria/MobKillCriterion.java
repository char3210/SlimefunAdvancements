package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.util.ConfigUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * this criterion is performed when a player kills a mob
 *
 */
public class MobKillCriterion extends Criterion {
    private final String entity;

    public static MobKillCriterion loadFromConfig(ConfigurationSection config) {
        String id = config.getName();

        int amount = config.getInt("amount");
        if (amount == 0) {
            amount = 1;
        }

        String name = config.getString("name");
        if(name == null) {
            name = id;
        }

        String entity = config.getString("entity");
        if (entity == null) {
            SFAdvancements.warn("entity not provided for " + id);
            return null;
        }

        return new MobKillCriterion(id, amount, name, entity);
    }

    public MobKillCriterion(String id, int amount, String name, String entity) {
        super(id, amount, name);
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }
}

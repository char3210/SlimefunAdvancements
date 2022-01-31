package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Function;

public class CriteriaTypes {
    private CriteriaTypes() {}

    public static void loadDefaultCriteria() {
        SFAdvancements.getRegistry().getCriterionTypes().put("consume", ConsumeCriterion::loadFromConfig);
        SFAdvancements.getRegistry().getCriterionTypes().put("interact", InteractCriterion::loadFromConfig);
        SFAdvancements.getRegistry().getCriterionTypes().put("inventory", InventoryCriterion::loadFromConfig);
        SFAdvancements.getRegistry().getCriterionTypes().put("multiblock", MultiBlockCriterion::loadFromConfig);
        SFAdvancements.getRegistry().getCriterionTypes().put("place", PlaceCriterion::loadFromConfig);
        SFAdvancements.getRegistry().getCriterionTypes().put("research", ResearchCriterion::loadFromConfig);
        SFAdvancements.getRegistry().getCriterionTypes().put("none", Criterion::loadFromConfig);
    }

    public static Criterion loadFromConfig(String id, ConfigurationSection config) {
        String type = config.getString("type");
        if (type == null) {
            SFAdvancements.warn("You must specify a type for criterion " + id + " in advancements.yml");
            return null;
        }

        Function<ConfigurationSection, Criterion> function = SFAdvancements.getRegistry().getCriterionTypes().get(type);
        if(function == null) {
            SFAdvancements.warn("unknown criterion type: " + type);
            return null;
        }

        return function.apply(config);
    }
}

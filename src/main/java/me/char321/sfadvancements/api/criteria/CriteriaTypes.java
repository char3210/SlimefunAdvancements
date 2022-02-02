package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Function;

public class CriteriaTypes {
    private CriteriaTypes() {}

    public static void loadDefaultCriteria() {
        putType("consume", ConsumeCriterion::loadFromConfig);
        putType("interact", InteractCriterion::loadFromConfig);
        putType("inventory", InventoryCriterion::loadFromConfig);
        putType("multiblock", MultiBlockCriterion::loadFromConfig);
        putType("place", PlaceCriterion::loadFromConfig);
        putType("research", ResearchCriterion::loadFromConfig);
        putType("none", Criterion::loadFromConfig);
    }

    public static void putType(String type, Function<ConfigurationSection, Criterion> criterionFromConfig) {
        SFAdvancements.getRegistry().getCriterionTypes().put(type, criterionFromConfig);
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

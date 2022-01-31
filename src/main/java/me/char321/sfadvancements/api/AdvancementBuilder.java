package me.char321.sfadvancements.api;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.CriteriaTypes;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.util.ConfigUtils;
import me.char321.sfadvancements.util.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AdvancementBuilder {
    private NamespacedKey key;
    private AdvancementGroup group;
    private ItemStack display;
    private String name;
    private List<Criterion> criteria = new ArrayList<>();

    public static AdvancementBuilder loadFromConfig(String key, ConfigurationSection config) {
        AdvancementBuilder builder = new AdvancementBuilder();
        builder.key(Utils.keyOf(key));
        builder.group(config.getString("group"));
        builder.display(ConfigUtils.getItem(config, "display"));
        builder.name(config.getString("name"));
        ConfigurationSection cripath = config.getConfigurationSection("criteria");
        if (cripath == null) {
            SFAdvancements.warn("criteria must be specified for advancement " + key);
            return null;
        }
        List<Criterion> criteria = new ArrayList<>();
        for (String id : cripath.getKeys(false)) {
            Criterion criterion = CriteriaTypes.loadFromConfig(id, cripath.getConfigurationSection(id));
            if (criterion != null) {
                criteria.add(criterion);
            }
        }
        builder.criteria(criteria.toArray(new Criterion[0]));
        return builder;
    }

    public AdvancementBuilder key(NamespacedKey key) {
        this.key = key;
        return this;
    }

    public AdvancementBuilder group(String group) {
        for (AdvancementGroup advgroup : SFAdvancements.getRegistry().getAdvancementGroups()) {
            if (advgroup.getId().equals(group)) {
                this.group = advgroup;
                return this;
            }
        }
        SFAdvancements.warn("unknown group: " + group);
        return this;
    }

    public AdvancementBuilder group(AdvancementGroup group) {
        this.group = group;
        return this;
    }

    public AdvancementBuilder display(ItemStack display) {
        this.display = display;
        return this;
    }

    public AdvancementBuilder name(String name) {
        this.name = name;
        return this;
    }

    public AdvancementBuilder criteria(Criterion... criteria) {
        this.criteria.addAll(List.of(criteria));
        return this;
    }

    public void register() {
        for (Criterion criterion : criteria) {
            criterion.setAdvancement(key);
            criterion.register();
        }
        Advancement adv = new Advancement(key, group, display, name, criteria.toArray(new Criterion[0]));
        adv.register();
    }

    public Advancement toAdvancement() {
        return new Advancement(key, group, display, name, criteria.toArray(new Criterion[0]));
    }

}

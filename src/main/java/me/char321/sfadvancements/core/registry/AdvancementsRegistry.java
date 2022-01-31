package me.char321.sfadvancements.core.registry;

import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.api.AdvancementGroup;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.core.criteria.completer.CriterionCompleter;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class AdvancementsRegistry {
    private final List<AdvancementGroup> advancementGroups = new ArrayList<>();
    private final Map<NamespacedKey, Advancement> advancements = new HashMap<>();
    private final Map<Class<? extends Criterion>, CriterionCompleter> completers = new HashMap<>();
    private final Map<String, Function<ConfigurationSection, Criterion>> criteriontypes = new HashMap<>();

    public List<AdvancementGroup> getAdvancementGroups() {
        return advancementGroups;
    }

    public Map<NamespacedKey, Advancement> getAdvancements() {
        return advancements;
    }

    public Advancement getAdvancement(NamespacedKey key) {
        return advancements.get(key);
    }

    public Map<Class<? extends Criterion>, CriterionCompleter> getCompleters() {
        return completers;
    }

    public CriterionCompleter getCompleter(Criterion cri) {
        return completers.get(cri.getClass());
    }

    public Map<String, Function<ConfigurationSection, Criterion>> getCriterionTypes() {
        return criteriontypes;
    }
}

package me.char321.sfadvancements.core.registry;

import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.api.AdvancementGroup;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancementsRegistry {
    private final List<AdvancementGroup> advancementGroups = new ArrayList<>();
    private final Map<NamespacedKey, Advancement> advancements = new HashMap<>();

    public List<AdvancementGroup> getAdvancementGroups() {
        return advancementGroups;
    }

    public Map<NamespacedKey, Advancement> getAdvancements() {
        return advancements;
    }

    public Advancement getAdvancement(NamespacedKey key) {
        return advancements.get(key);
    }
}

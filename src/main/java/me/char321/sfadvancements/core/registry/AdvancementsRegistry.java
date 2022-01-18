package me.char321.sfadvancements.core.registry;

import me.char321.sfadvancements.api.AdvancementGroup;

import java.util.ArrayList;
import java.util.List;

public class AdvancementsRegistry {
    private final List<AdvancementGroup> advancementGroups = new ArrayList<>();

    public List<AdvancementGroup> getAdvancementGroups() {
        return advancementGroups;
    }
}

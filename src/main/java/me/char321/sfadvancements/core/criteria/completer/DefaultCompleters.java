package me.char321.sfadvancements.core.criteria.completer;

import me.char321.sfadvancements.SFAdvancements;

public class DefaultCompleters {
    public static void registerDefaultCompleters() {
        InteractCriterionCompleter interact = new InteractCriterionCompleter();
        SFAdvancements.getRegistry().getCompleters().put(interact.getCriterionClass(), interact);

        InventoryCriterionCompleter inventory = new InventoryCriterionCompleter();
        SFAdvancements.getRegistry().getCompleters().put(inventory.getCriterionClass(), inventory);

        PlaceCriterionCompleter place = new PlaceCriterionCompleter();
        SFAdvancements.getRegistry().getCompleters().put(place.getCriterionClass(), place);

    }
}

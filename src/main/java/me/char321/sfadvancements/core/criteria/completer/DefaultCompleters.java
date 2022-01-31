package me.char321.sfadvancements.core.criteria.completer;

import me.char321.sfadvancements.SFAdvancements;

public class DefaultCompleters {

    private DefaultCompleters() {

    }

    public static void registerDefaultCompleters() {
        register(new InteractCriterionCompleter());
        register(new InventoryCriterionCompleter());
        register(new PlaceCriterionCompleter());
        register(new ResearchCriterionCompleter());
        register(new MultiBlockCriterionCompleter());
    }

    private static void register(CriterionCompleter completer) {
        SFAdvancements.getRegistry().getCompleters().put(completer.getCriterionClass(), completer);
    }
}

package me.char321.sfadvancements.core.criteria.completer;

import me.char321.sfadvancements.SFAdvancements;

public class DefaultCompleters {
    public static void registerDefaultCompleters() {
        InteractCriterionCompleter interact = new InteractCriterionCompleter();
        SFAdvancements.getRegistry().getCompleters().put(interact.getCriterionClass(), interact);
    }
}

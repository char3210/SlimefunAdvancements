package me.char321.sfadvancements.core.criteria.completer;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.Criterion;

public interface CriterionCompleter {
    void register(Criterion criterion);

    default void register() {
        SFAdvancements.getRegistry().getCompleters().put(this.getCriterionClass(), this);
    }

    Class<? extends Criterion> getCriterionClass();

    /**
     * when this is called don't complete criteria previously registered via register(Criterion)
     * typically this is done by clearing the collection/map that held the criteria
     */
    void reload();
}

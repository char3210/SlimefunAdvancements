package me.char321.sfadvancements.core.criteria.completer;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.Criterion;

public interface CriterionCompleter {
    void register(Criterion criterion);

    default void register() {
        SFAdvancements.getRegistry().getCompleters().put(this.getCriterionClass(), this);
    }

    Class<? extends Criterion> getCriterionClass();

    void reload();
}

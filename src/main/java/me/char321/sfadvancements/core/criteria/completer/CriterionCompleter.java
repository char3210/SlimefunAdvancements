package me.char321.sfadvancements.core.criteria.completer;

import me.char321.sfadvancements.api.criteria.Criterion;

public interface CriterionCompleter {
    void register(Criterion cri);

    Class<? extends Criterion> getCriterionClass();
}

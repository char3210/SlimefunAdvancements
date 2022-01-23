package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.NamespacedKey;

import java.util.UUID;

public abstract class CountCriterion extends Criterion {
    protected int amount;

    public CountCriterion(NamespacedKey adv, int amount) {
        super(adv);
        this.amount = amount;
    }

    public boolean increment(UUID player)  {
        SFAdvancements.getAdvManager().completeCriteria(player, this);
    }
}

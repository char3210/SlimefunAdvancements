package me.char321.sfadvancements.api.criteria;

import org.bukkit.NamespacedKey;

/**
 * that means this also has to be immutable
 */
public class Criterion {
    protected final NamespacedKey advancement;
    private final String id;
    private final int count;

    public Criterion(NamespacedKey adv, String id) {
        this(adv, id, 1);
    }

    public Criterion(NamespacedKey adv, String id, int count) {
        this.advancement = adv;
        this.id = id;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public int getCount() {
        //yay technical debt
        return count;
    }

    public NamespacedKey getAdvancement() {
        return advancement;
    }

}

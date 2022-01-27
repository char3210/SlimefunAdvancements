package me.char321.sfadvancements.api.criteria;

import org.bukkit.NamespacedKey;

/**
 * for when you want players to unlock when they unlock a specific research
 */
public class ResearchCriterion extends Criterion {
    private NamespacedKey research;

    public ResearchCriterion(String id, NamespacedKey research) {
        super(id);
        this.research = research;
    }

    public NamespacedKey getResearch() {
        return research;
    }
}

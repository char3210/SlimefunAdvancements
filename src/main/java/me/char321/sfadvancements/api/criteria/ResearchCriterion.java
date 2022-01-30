package me.char321.sfadvancements.api.criteria;

import org.bukkit.NamespacedKey;

/**
 * this criterion is performed when a player unlocks a specific research
 */
public class ResearchCriterion extends Criterion {
    private NamespacedKey research;

    /**
     * creates a new researchcriterion
     *
     * @param id the id of the criterion (should be unique per advancement)
     * @param research the namespaced key of the research
     */
    public ResearchCriterion(String id, NamespacedKey research) {
        super(id);
        this.research = research;
    }

    /**
     * gets the {@link NamespacedKey} of the research to be completed for this criterion
     *
     * @return the research that must be completed
     */
    public NamespacedKey getResearch() {
        return research;
    }
}

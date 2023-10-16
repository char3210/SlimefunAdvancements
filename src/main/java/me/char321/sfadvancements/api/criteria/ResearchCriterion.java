package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;

/**
 * this criterion is performed when a player unlocks a specific research
 */
public class ResearchCriterion extends Criterion {
    private NamespacedKey research;

    public static ResearchCriterion loadFromConfig(ConfigurationSection config) {
        String id = config.getName();

        String name = config.getString("name");
        if(name == null) {
            name = id;
        }

        name = ChatColor.translateAlternateColorCodes('&', name);

        String research = config.getString("research");
        if (research == null) {
            SFAdvancements.warn("specify a research for criterion " + id);
            return null;
        }

        return new ResearchCriterion(id, name, NamespacedKey.fromString(research));
    }

    /**
     * creates a new researchcriterion
     *
     * @param id the id of the criterion (should be unique per advancement)
     * @param research the namespaced key of the research
     */
    public ResearchCriterion(String id, String name, NamespacedKey research) {
        super(id, name);
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

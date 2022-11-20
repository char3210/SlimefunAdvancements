package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.core.criteria.completer.CriterionCompleter;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * an immutable base class that is only completable via commands
 * useful for custom configurable criteria maybe?
 * extend this class and create a {@link CriterionCompleter} for it
 *
 * @see CriterionCompleter
 * @author char321
 */
public class Criterion {
    private NamespacedKey advancement;
    private final String id;
    private final String name;
    private final int count;

    public Criterion(String id, int count, String name) {
        this.id = id;
        this.count = count;
        this.name = name;
    }

    public Criterion(String id) {
        this(id, 1);
    }

    public Criterion(String id, int count) {
        this(id, count, id);
    }

    public Criterion(String id, String name) {
        this(id, 1, name);
    }


    /**
     * why is this public this should be in criterion builder class maybe
     *
     * @param config the configurationsection to create a criterion from
     * @return the created criterion
     */
    public static Criterion loadFromConfig(ConfigurationSection config) {
        String id = config.getName();

        int amount = config.getInt("amount");
        if (amount == 0) {
            amount = 1;
        }

        String name = config.getString("name");
        if(name == null) {
            name = id;
        }

        return new Criterion(id, amount, name);
    }

    /**
     * gets the criterion id; should be unique within the advancement
     *
     * @return the id of the criterion
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the count of the criterion, or how many times the criterion must be performed for it to complete
     *
     * @return the count of the criterion
     */
    public int getCount() {
        return count;
    }


    public String getName() {
        return name;
    }

    /**
     * Gets the key of the advancement that is on this criterion
     * (please don't put the same criterion in multiple advancements)
     *
     * @return the key of the advancement
     */
    public NamespacedKey getAdvancement() {
        return advancement;
    }

    /**
     * don't use this it'll probably get removed soon due to configurability
     */
    public void setAdvancement(NamespacedKey advancement) {
        this.advancement = advancement;
    }

    /**
     * registers the criterion so that it can be completed
     */
    public void register() {
        SFAdvancements.getRegistry().getCompleter(this).register(this);
    }

    /**
     * utility method for doing this criterion
     * increments the progress for that player of this criterion by 1
     *
     * @param p uuid of player to perform the criterion
     */
    public void perform(UUID p) {
        SFAdvancements.getAdvManager().getProgress(p).doCriterion(this);
    }

    /**
     * utility method for doing this criterion
     * increments the progress for that player of this criterion by 1
     *
     * @param p player to perform the criterion
     */
    public void perform(Player p) {
        this.perform(p.getUniqueId());
    }

    /**
     * finishes this criterion completely
     *
     * @param p the player to complete the criterion
     */
    public void complete(Player p) {
        SFAdvancements.getAdvManager().getProgress(p).completeCriterion(this);
    }
}

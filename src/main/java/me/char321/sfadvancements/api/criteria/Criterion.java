package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.core.criteria.completer.CriterionCompleter;
import me.char321.sfadvancements.util.ConfigUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
    private final int count;

    public Criterion(String id) {
        this(id, 1);
    }

    public Criterion(String id, int count) {
        this.id = id;
        this.count = count;
    }

    /**
     * why is this public this should be in criterion builder class maybe
     *
     * @param id id of the criterion
     * @param config the configurationsection to create a criterion from
     * @return the created criterion
     */
    public static Criterion loadFromConfig(String id, ConfigurationSection config) {
        String type = config.getString("type");
        if (type == null) {
            SFAdvancements.warn("You must specify a type for criterion " + id + " in advancements.yml");
            return null;
        }
        switch(type) { //TODO: allow other plugins to add and make this overall better
            case "interact":
                int amount = config.getInt("amount");
                if (amount == 0) {
                    amount = 1;
                }
                ItemStack item = ConfigUtils.getItem(config, "item");
                if (item == null) {
                    SFAdvancements.warn("unknown item for interact criterion " + id);
                    return null;
                }
                return new InteractCriterion(id, amount, item);
            case "inventory":
                item = ConfigUtils.getItem(config, "item");
                if (item == null) {
                    SFAdvancements.warn("unknown item for inventory criterion " + id);
                    return null;
                }
                return new InventoryCriterion(id, item);
            case "place":
                item = ConfigUtils.getItem(config, "item");
                if (item == null) {
                    SFAdvancements.warn("unknown item for place criterion " + id);
                    return null;
                }
                amount = config.getInt("amount");
                if (amount == 0) {
                    amount = 1;
                }
                return new PlaceCriterion(id, amount, item);
            case "research":
                String research = config.getString("research");
                if (research == null) {
                    SFAdvancements.warn("specify a research for criterion " + id);
                    return null;
                }
                return new ResearchCriterion(id, NamespacedKey.fromString(research));
            case "none":
                amount = config.getInt("amount");
                if (amount == 0) {
                    amount = 1;
                }
                return new Criterion(id, amount);
            default:
                SFAdvancements.warn("unknown criterion type: " + type);
                return null;
        }
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

}

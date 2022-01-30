package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.util.ConfigUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * that means this also has to be immutable
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
            default:
                SFAdvancements.warn("unknown criterion type: " + type);
                return null;
        }
    }

    public String getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public NamespacedKey getAdvancement() {
        return advancement;
    }

    public void setAdvancement(NamespacedKey advancement) {
        this.advancement = advancement;
    }

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

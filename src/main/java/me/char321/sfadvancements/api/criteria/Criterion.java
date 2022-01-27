package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

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

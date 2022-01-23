package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * impractical, only for example purposes
 */
public class InteractCriterion extends Criterion implements Listener {
    private final ItemStack item;

    /**
     * this criterion is iterated when a player right clicks an item
     *
     * @param advancement the key of the advancement
     * @param id the id of the criteria (should not be the same as other criteria in the same advancement)
     * @param amount how many times the player has to click
     * @param item the item player clicks
     */
    public InteractCriterion(NamespacedKey advancement, String id, int amount, ItemStack item) {
        super(advancement, id, amount);
        this.item = item;

        SFAdvancements.getRegistry().getCompleter(this).register(this);
    }

    public ItemStack getItem() {
        return item;
    }
}

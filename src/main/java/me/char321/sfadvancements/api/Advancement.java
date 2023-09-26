package me.char321.sfadvancements.api;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.api.reward.Reward;
import me.char321.sfadvancements.util.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * an advancement that appears in the advancements gui and players can complete
 * when all of an advancement's criteria are completed, it becomes shiny oooh
 */
public class Advancement {
    private final NamespacedKey key;
    private final NamespacedKey parent;
    private final AdvancementGroup group;
    private final ItemStack display;
    private final String name;
    private final boolean hidden;
    private final Criterion[] criteria;
    private final Reward[] rewards;

    public Advancement(NamespacedKey key, @Nullable NamespacedKey parent, AdvancementGroup group, ItemStack display, String name, boolean hidden, Criterion[] criteria, Reward[] rewards) {
        this.key = key;
        if (parent == null) {
            parent = Utils.keyOf(group.getId());
        }
        this.parent = parent;
        this.group = group;
        this.display = display;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.hidden = hidden;
        this.criteria = criteria;
        this.rewards = rewards;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public NamespacedKey getParent() {
        return parent;
    }

    public AdvancementGroup getGroup() {
        return group;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public String getName() {
        return name;
    }

    public boolean isHidden() {
        return hidden;
    }

    public Criterion[] getCriteria() {
        return criteria;
    }

    public Reward[] getRewards() {
        return rewards;
    }

    /**
     * returns a criterion based on its on id
     * @param id the id of the criterion
     * @return the found criterion, null otherwise
     */
    @Nullable
    public Criterion getCriterion(String id) {
        for (Criterion criterion : criteria) {
            if (criterion.getId().equals(id)) {
                return criterion;
            }
        }
        return null;
    }

    public String getDescription() {
        String res = getName().replaceAll("[\\[\\]]", "");
        res += "\n";
        if (display.hasItemMeta()) {
            ItemMeta im = display.getItemMeta();
            if (im != null && im.hasLore()) {
                res += String.join(" ", im.getLore());
            }
        }
        return res;
    }

    public void register() {
        group.addAdvancement(this);
//        group.getAdvancements().add(this);
        SFAdvancements.getRegistry().getAdvancements().put(key, this);
    }

    /**
     * sends the message of completion and gives rewards
     *
     * @param p player
     */
    public void onComplete(Player p) {
        for (Criterion criterion : criteria) {
            criterion.complete(p);
        }

        for (Reward reward : rewards) {
            reward.give(p);
        }

        broadcastMessage(p);
    }

    public void onRevoke(Player p) {
        if (SFAdvancements.getMainConfig().getBoolean("use-advancements-api")) {
            SFAdvancements.getVanillaHook().revoke(p, this.getKey());
        }
    }

    private void broadcastMessage(Player p) {
        BaseComponent component = new TextComponent();
        component.addExtra(p.getName() + " has made the advancement ");
        BaseComponent sub = new TextComponent(getName());
        sub.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(getDescription())));
        component.addExtra(sub);
        Bukkit.spigot().broadcast(component);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advancement that = (Advancement) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}

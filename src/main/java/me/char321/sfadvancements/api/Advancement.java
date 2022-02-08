package me.char321.sfadvancements.api;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.api.reward.Reward;
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

import java.util.Objects;

/**
 * an advancement that appears in the advancements gui and players can complete
 * when all of an advancement's criteria are completed, it becomes shiny oooh
 */
public class Advancement {
    private NamespacedKey key;
    private AdvancementGroup group;
    private ItemStack display;
    private String name;
    private Criterion[] criteria;
    private Reward[] rewards;

    public Advancement(NamespacedKey key, AdvancementGroup group, ItemStack display, String name, Criterion[] criteria, Reward[] rewards) {
        this.key = key;
        this.group = group;
        this.display = display;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.criteria = criteria;
        this.rewards = rewards;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public String getName() {
        return name;
    }

    public Criterion[] getCriteria() {
        return criteria;
    }

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
        group.getAdvancements().add(this);
        SFAdvancements.getRegistry().getAdvancements().put(key, this);
    }

    /**
     * sends the message of completion, should move out of this class //TODO
     *
     * @param p player
     */
    public void complete(Player p) {
        BaseComponent component = new TextComponent();
        component.addExtra(p.getName() + " has made the advancement ");
        BaseComponent sub = new TextComponent(getName());
        sub.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(getDescription())));
        component.addExtra(sub);
        Bukkit.spigot().broadcast(component);

        for (Reward reward : rewards) {
            reward.give(p);
        }
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

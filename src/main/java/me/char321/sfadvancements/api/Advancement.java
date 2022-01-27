package me.char321.sfadvancements.api;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.Criterion;
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
 * i'm gonna make this immutable becaus eyes
 */
public class Advancement {
    private NamespacedKey key;
    private AdvancementGroup group;
    private ItemStack display;
    private String name;
    private Criterion[] criteria;

    public Advancement(NamespacedKey key, AdvancementGroup group, ItemStack display, String name, Criterion... criteria) {
        this.key = key;
        this.group = group;
        this.display = display;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.criteria = criteria;
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
     * please call this
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

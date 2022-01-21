package me.char321.sfadvancements.api;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import me.char321.sfadvancements.SFAdvancements;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.stream.Collectors;

public class Advancement {
    private NamespacedKey key;
    private AdvancementGroup group;
    private ItemStack display;
    private String name;

    public Advancement(NamespacedKey key, AdvancementGroup group, ItemStack display, String name) {
        this.key = key;
        this.group = group;
        this.display = display;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
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

    public String getDescription() {
        if(display.hasItemMeta()) {
            ItemMeta im = display.getItemMeta();
            if(im != null && im.hasLore()) {
                return String.join(" ", im.getLore());
            }
        }
        return "";
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
    public boolean complete(Player p) {
        if(SFAdvancements.getAdvManager().complete(p, this)) {
            BaseComponent component = new TextComponent();
            component.addExtra(p.getName() + " has made the advancement ");
            BaseComponent sub = new TextComponent(name);
            sub.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new Text(name.replaceAll("[\\[\\]]", "")),
                    new Text(getDescription())
            ));
            component.addExtra(sub);
            Bukkit.spigot().broadcast(component);
//            Bukkit.broadcastMessage(p.getName() + " has completed the advancement " + name);
            return true;
        }
        return false;
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

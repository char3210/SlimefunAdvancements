package me.char321.sfadvancements.api;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Advancement {
    private NamespacedKey key;
    private AdvancementGroup group;
    private ItemStack display;
    private String name;

    public Advancement(NamespacedKey key, AdvancementGroup group, ItemStack display, String name) {
        this.display = display;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public ItemStack getDisplay() {
        return display;
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
        if(!SFAdvancements.instance().getAdvManager().isCompleted(p, this)) {
            Bukkit.broadcastMessage(p.getName() + " has completed the advancement " + name);
        }
        SFAdvancements.instance().getAdvManager().complete(p, this);
    }
}

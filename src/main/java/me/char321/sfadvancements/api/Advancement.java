package me.char321.sfadvancements.api;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Advancement {
    private ItemStack display;
    private String id;

    public Advancement(ItemStack display, String id) {
        this.display = display;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ItemStack getDisplay() {
        return display;
    }

    /**
     * please call this
     *
     * @param p player
     */
    public void complete(Player p) {
        SFAdvancements.instance().getAdvManager().complete(p, this);
        Bukkit.broadcastMessage(p.getName() + " has completed the advancement " + id);
    }
}

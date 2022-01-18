package me.char321.sfadvancements.api;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Advancement {
    private SlimefunAddon addon;
    private ItemStack display;
    private String name;

    public Advancement(SlimefunAddon addon, ItemStack display, String name) {
        this.addon = addon;
        this.name = name;
    }

    public void register() {

    }

    public void complete(Player p) {

    }
}

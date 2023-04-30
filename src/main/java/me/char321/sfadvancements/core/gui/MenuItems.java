package me.char321.sfadvancements.core.gui;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MenuItems {
    private MenuItems() {}

    public static final ItemStack BLACK = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " ");
    public static final ItemStack GRAY = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " ");
    public static final ItemStack YELLOW = new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE, " ");
    public static final ItemStack BACK_ITEM = new CustomItemStack(
            Material.ENCHANTED_BOOK, meta -> {
        final List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY + ChatColors.color(Slimefun.getLocalization().getMessage("guide.back.guide")));
        meta.setDisplayName(ChatColors.color("&7⇦ " + Slimefun.getLocalization().getMessage("guide.back.title")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(lore);
    });
}

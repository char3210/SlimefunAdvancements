package me.char321.sfadvancements.util;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Utils {
    private Utils() {}

    public static ItemStack makeShiny(ItemStack item) {
        item = item.clone();
        ItemMeta im = item.getItemMeta();
        im.addEnchant(Enchantment.DURABILITY, 1, false);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(im);
        return item;
    }

    public static void makeShiny(ItemMeta im) {
        im.addEnchant(Enchantment.DURABILITY, 1, false);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public static boolean keyIsSFA(NamespacedKey key) {
        return key.getNamespace().equals(SFAdvancements.instance().getName().toLowerCase(Locale.ROOT));
    }

    public static NamespacedKey keyOf(String value) {
        return new NamespacedKey(SFAdvancements.instance(), value);
    }

    public static Advancement fromKey(String value) {
        return SFAdvancements.getRegistry().getAdvancement(keyOf(value));
    }

    public static Advancement fromKey(NamespacedKey value) {
        return SFAdvancements.getRegistry().getAdvancement(value);
    }

    public static boolean isValidAdvancement(NamespacedKey key) {
        return SFAdvancements.getRegistry().getAdvancements().containsKey(key);
    }

    public static void listen(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, SFAdvancements.instance());
    }

    public static Map<ItemStack, Integer> getContents(Inventory inv) {
        Map<ItemStack, Integer> contents = new HashMap<>();
        for (ItemStack item : inv) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }
            ItemStack clone = item.clone();
            clone.setAmount(1);
            contents.merge(clone, item.getAmount(), Integer::sum);
        }
        return contents;
    }

    public static void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(SFAdvancements.instance(), runnable);
    }

    public static void runLater(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(SFAdvancements.instance(), runnable, delay);
    }
}

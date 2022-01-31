package me.char321.sfadvancements.util;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
}

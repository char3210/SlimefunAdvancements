package me.char321.sfadvancements.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {
    public static ItemStack makeShiny(ItemStack item) {
        item = item.clone();
        ItemMeta im = item.getItemMeta();
        im.addEnchant(Enchantment.DURABILITY, 1, false);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(im);
        return item;
    }
}

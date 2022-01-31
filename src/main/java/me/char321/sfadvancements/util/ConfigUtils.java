package me.char321.sfadvancements.util;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ConfigUtils {
    private ConfigUtils() {}

    public static String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static ItemStack getItem(ConfigurationSection config, String path) {
        if (config.isItemStack(path)) {
            return config.getItemStack(path);
        }

        ConfigurationSection itemSection = config.getConfigurationSection(path);
        if (itemSection == null) {
            return null;
        }

        String type = itemSection.getString("type");
        ItemStack item;
        try {
            item = getTemplate(type).clone();
        } catch (IllegalArgumentException x) {
            SFAdvancements.warn("invalid item type " + type);
            return null;
        }

        ItemMeta im = item.getItemMeta();

        String name = itemSection.getString("name");
        if(name != null) {
            im.setDisplayName(translate(name));
        }

        List<String> lore = itemSection.getStringList("lore");
        lore.replaceAll(ConfigUtils::translate);
        im.setLore(lore);

        item.setItemMeta(im);
        return item;
    }

    public static ItemStack getTemplate(String id) {
        if (id == null || id.equals("AIR") || id.equals("null")) {
            return new ItemStack(Material.AIR);
        }

        SlimefunItem item = SlimefunItem.getById(id);
        if (item != null) {
            return item.getItem();
        }
        
        Material material = Material.getMaterial(id);
        if (material != null) {
            return new ItemStack(material);
        }

        throw new IllegalArgumentException();
    }
}

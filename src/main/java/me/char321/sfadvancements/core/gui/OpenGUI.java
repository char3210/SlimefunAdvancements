package me.char321.sfadvancements.core.gui;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.api.AdvancementGroup;
import me.char321.sfadvancements.core.registry.AdvancementsRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OpenGUI {
    private final Inventory inventory;
    private final AdvancementsRegistry registry;
    private int page = 1;
    private int groupIndex = 0;
    private int scroll = 0;

    public OpenGUI() {
        this.inventory = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Advancements");
        this.registry = SFAdvancements.getRegistry();
        refresh();
    }

    public Inventory getInventory() {
        refresh();
        return inventory;
    }

    public void click(int slot) {
        if(slot == 1 && page > 1) {
            page--;
        } else if(slot == 7) {
            int maxPage = (registry.getAdvancementGroups().size() - 1) / 5 + 1;
            if(page + 1 <= maxPage) {
                page++;
            }
        } else if(slot > 1 && slot < 7) {
            int possibleIndex = 5 * (page - 1) + (slot - 2);
            if(registry.getAdvancementGroups().size() > possibleIndex) {
                groupIndex = possibleIndex;
                scroll = 0;
            }
        } else if(slot == 17 && scroll > 0) {
            scroll--;
        } else if(slot == 53) {
            AdvancementGroup group = registry.getAdvancementGroups().get(groupIndex);
            //make better
            int size = group.getAdvancements().size();
            int maxScroll = (size - 1) / 8 - 4;
            if(scroll + 1 <= maxScroll) {
                scroll++;
            }
        }
        refresh();
    }

    public void refresh() {
        //region Border
        inventory.setItem(0, MenuItems.GRAY);
        inventory.setItem(8, MenuItems.GRAY);
        //endregion
        //region Arrows
        int maxPage = (registry.getAdvancementGroups().size() - 1) / 5 + 1;
        String pageLore = "&7(" + page + " / " + maxPage + ")";

        ItemStack leftArrow;
        if(page == 1) {
            leftArrow = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, "&7Previous Page", pageLore);
        } else {
            leftArrow = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&ePrevious Page", pageLore);
        }
        inventory.setItem(1, leftArrow);

        ItemStack rightArrow;
        if(page == maxPage) {
            rightArrow = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, "&7Next Page", pageLore);
        } else {
            rightArrow = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&eNext Page", pageLore);
        }
        inventory.setItem(7, rightArrow);
        //endregion
        //region Advancement Groups
        for(int i=0;i<5;i++) {
            int slot = i+2;
            int dispIndex = 5 * (page - 1) + i;
            ItemStack display;
            if(registry.getAdvancementGroups().size() > dispIndex) {
                display = registry.getAdvancementGroups().get(dispIndex).getDisplayItem();
                if(dispIndex == groupIndex) {
                    display = display.clone();
                    ItemMeta im = display.getItemMeta();
                    im.addEnchant(Enchantment.DURABILITY, 1, true);
                    im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    display.setItemMeta(im);
                }
            } else {
                display = MenuItems.GRAY;
            }
            inventory.setItem(slot, display);
        }
        //endregion
        //region Advancements
        AdvancementGroup advancementGroup = registry.getAdvancementGroups().get(groupIndex);

        //endregion
    }

}

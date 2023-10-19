package me.char321.sfadvancements.core.gui;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.api.AdvancementGroup;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.core.registry.AdvancementsRegistry;
import me.char321.sfadvancements.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OpenGUI {
    private final Inventory inventory;
    private final AdvancementsRegistry registry = SFAdvancements.getRegistry();
    private final UUID playerUUID;
    private int page = 1;
    private int groupIndex = 0;
    private int scroll = 0;

    public OpenGUI(Player player) {
        this(player.getUniqueId());
    }

    public OpenGUI(UUID playerUUID) {
        this.inventory = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Advancements");
        this.playerUUID = playerUUID;
        refresh();
    }

    public Inventory getInventory() {
        refresh();
        return inventory;
    }

    public void click(Player player, int slot) {
        if (slot == 0) {
            PlayerProfile.find(player).ifPresent(profile -> SlimefunGuide.openMainMenu(profile, SlimefunGuideMode.SURVIVAL_MODE, profile.getGuideHistory().getMainMenuPage()));
        } else if (slot == 1 && page > 1) {
            page--;
        } else if (slot == 7) {
            int maxPage = (registry.getAdvancementGroups().size() - 1) / 5 + 1;
            if (page + 1 <= maxPage) {
                page++;
            }
        } else if (slot > 1 && slot < 7) {
            int possibleIndex = 5 * (page - 1) + (slot - 2);
            if (registry.getAdvancementGroups().size() > possibleIndex) {
                groupIndex = possibleIndex;
                scroll = 0;
            }
        } else if (slot == 17 && scroll > 0) {
            scroll--;
        } else if (slot == 53) {
            AdvancementGroup group = registry.getAdvancementGroups().get(groupIndex);
            //make better
            int size = group.getVisibleAdvancements(playerUUID).size();
            int maxScroll = (size - 1) / 8 - 4;
            if (scroll + 1 <= maxScroll) {
                scroll++;
            }
        }
        refresh();
    }

    public void refresh() {
        refreshBackButton();
        refreshStats();
        refreshArrows();
        refreshGroups();
        refreshScroll();
        refreshAdvancements();
    }

    private void refreshBackButton() {
        inventory.setItem(0, MenuItems.BACK_ITEM);
    }

    private void refreshStats() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerUUID));
        meta.setDisplayName(ChatColor.YELLOW + "Stats");
        StringBuilder completedadvancements = new StringBuilder();
        completedadvancements.append(ChatColor.GRAY).append("Completed Advancements: ");
        int completed = SFAdvancements.getAdvManager().getProgress(playerUUID).getCompletedAdvancements().size();
        int total = SFAdvancements.getRegistry().getAdvancements().size();
        if(completed == total) {
            completedadvancements.append(ChatColor.YELLOW);
        } else {
            completedadvancements.append(ChatColor.WHITE);
        }
        completedadvancements.append(completed).append(ChatColor.GRAY).append("/").append(total);
        List<String> lore = new ArrayList<>();
        lore.add(completedadvancements.toString());
        meta.setLore(lore);
        head.setItemMeta(meta);
        inventory.setItem(8, head);
    }

    private void refreshArrows() {
        int maxPage = (registry.getAdvancementGroups().size() - 1) / 5 + 1;
        String pageLore = "&7(" + page + " / " + maxPage + ")";

        ItemStack leftArrow;
        if (page == 1) {
            leftArrow = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, "&7Previous Page", pageLore);
        } else {
            leftArrow = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&ePrevious Page", pageLore);
        }
        inventory.setItem(1, leftArrow);

        ItemStack rightArrow;
        if (page == maxPage) {
            rightArrow = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, "&7Next Page", pageLore);
        } else {
            rightArrow = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&eNext Page", pageLore);
        }
        inventory.setItem(7, rightArrow);
    }

    private void refreshGroups() {
        for (int i=0;i<5;i++) {
            int slot = i+2;
            int dispIndex = 5 * (page - 1) + i;
            ItemStack display;
            if (registry.getAdvancementGroups().size() > dispIndex) {
                display = registry.getAdvancementGroups().get(dispIndex).getDisplayItem();
                if (dispIndex == groupIndex) {
                    display = Utils.makeShiny(display);
                }
            } else {
                display = MenuItems.GRAY;
            }
            inventory.setItem(slot, display);
        }
    }

    private void refreshScroll() {
        AdvancementGroup group = registry.getAdvancementGroups().get(groupIndex);
        inventory.setItem(26, MenuItems.YELLOW);
        inventory.setItem(35, MenuItems.YELLOW);
        inventory.setItem(44, MenuItems.YELLOW);

        ItemStack scrollUp;
        if (scroll == 0) {
            scrollUp = MenuItems.YELLOW;
        } else {
            scrollUp = new CustomItemStack(Material.ARROW, "&eScroll Up");
        }
        inventory.setItem(17, scrollUp);

        ItemStack scrollDown;
        int size = group.getVisibleAdvancements(playerUUID).size();
        int maxScroll = (size - 1) / 8 - 4;
        if (scroll >= maxScroll) {
            scrollDown = MenuItems.YELLOW;
        } else {
            scrollDown = new CustomItemStack(Material.ARROW, "&eScroll Down");
        }
        inventory.setItem(53, scrollDown);
    }

    private void refreshAdvancements() {
        AdvancementGroup group = registry.getAdvancementGroups().get(groupIndex);
        List<Advancement> advancements = group.getVisibleAdvancements(playerUUID);
        for (int i = 0; i < 40; i++) {
            int row = i / 8 + 1;
            int col = i % 8;
            int slot = row * 9 + col;

            int advindex = i + 8 * scroll;
            ItemStack display = null;
            if (advindex < advancements.size()) {
                Advancement adv = advancements.get(advindex);
                display = getDisplayFor(adv);
            }

            inventory.setItem(slot, display);
        }
    }

    private ItemStack getDisplayFor(Advancement adv) {
        ItemStack display = adv.getDisplay().clone();
        ItemMeta displayim = display.getItemMeta();
        if (displayim == null) {
            throw new IllegalArgumentException("display item meta is null");
        }

        displayim.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if (SFAdvancements.getAdvManager().isCompleted(playerUUID, adv)) {
            Utils.makeShiny(displayim);
        }

        List<String> lore = displayim.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        boolean loreAdded = false;
        for (int i = lore.size() - 1; i >= 0; i--) {
            if ("%criteria%".equals(lore.get(i))) {
                lore.remove(i);
                lore.addAll(i, this.getCriteriaLore(adv));
                loreAdded = true;
            }
        }
        if (!loreAdded) {
            lore.addAll(this.getCriteriaLore(adv));
        }

        displayim.setLore(lore);
        display.setItemMeta(displayim);
        return display;
    }

    private List<String> getCriteriaLore(Advancement adv) {
        List<String> res = new ArrayList<>();
        for (Criterion criterion : adv.getCriteria()) {
            String criterionName = criterion.getName();
            int progress = SFAdvancements.getAdvManager().getCriterionProgress(playerUUID, criterion);
            int max = criterion.getCount();
            boolean cridone = progress >= max;
            res.add(ChatColor.GRAY + criterionName + ": " + (cridone ? ChatColor.YELLOW : ChatColor.WHITE) + progress + "/" + max);
        }
        return res;
    }
}

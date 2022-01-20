package me.char321.sfadvancements.implementation;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.api.AdvancementGroup;
import me.char321.sfadvancements.implementation.basic.GettingStarted;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DefaultAdvancements {
    public static void registerDefaultAdvancements() {
        AdvancementGroup basic = new AdvancementGroup(new CustomItemStack(Material.SLIME_BALL, "Basic"));
        List<Advancement> basicadvs = basic.getAdvancements();
        for(int i = 1; i <= 100; i++) {
            ItemStack display = new CustomItemStack(Material.POTATO, Integer.toString(i));
            Advancement adv = new Advancement(display, "what");
            basicadvs.add(adv);
        }
        basicadvs.add(new GettingStarted(new CustomItemStack(Material.CRAFTING_TABLE), "gettingstarted"));
        basic.register();

        AdvancementGroup electric = new AdvancementGroup(new CustomItemStack(Material.REDSTONE));
        List<Advancement> electricadvs = electric.getAdvancements();
        electricadvs.add(new Advancement(new CustomItemStack(Material.OAK_BOAT), "e"));
        electric.register();
    }
}

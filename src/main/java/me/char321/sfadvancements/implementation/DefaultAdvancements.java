package me.char321.sfadvancements.implementation;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.api.AdvancementBuilder;
import me.char321.sfadvancements.api.AdvancementGroup;
import me.char321.sfadvancements.api.AdvancementType;
import me.char321.sfadvancements.implementation.basic.GettingStartedListener;
import me.char321.sfadvancements.util.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DefaultAdvancements {
    static AdvancementGroup basic = new AdvancementGroup(new CustomItemStack(Material.SLIME_BALL,
            "&fBasic",
            "&7&oThe core spirit of Slimefun."));
    static AdvancementGroup electric = new AdvancementGroup(new CustomItemStack(Material.REDSTONE,
            "&eElectric",
            "&7&oThe center of civilization."));

    public static void registerDefaultAdvancements() {
        new AdvancementBuilder()
                .key(Utils.keyOf("gettingstarted"))
                .group(basic)
                .display(new CustomItemStack(Material.CRAFTING_TABLE,
                        "&aGetting Started",
                        "Construct and use an Enhanced Crafting Table"))
                .name("&a[Getting Started]")
                .register();
        new GettingStartedListener();

        new AdvancementBuilder()
                .key(Utils.keyOf("portable_workbench"))
                .group(basic)
                .display(new CustomItemStack(Material.CRAFTING_TABLE,
                        "&aPortable Workbench",
                        "Craft a Portable Workbench"))
                .name("&a[Portable Workbench]")
                .type(AdvancementType.INVENTORY)
                .goal(SlimefunItems.PORTABLE_CRAFTER)
                .register();

        basic.register();

        new AdvancementBuilder()
                .key(Utils.keyOf("energy_regulator"))
                .group(electric)
                .display(new CustomItemStack(SlimefunItems.ENERGY_REGULATOR,
                        "&aEnergy Regulator",
                        "Craft an Energy Regulator"))
                .name("&a[Energy Regulator]")
                .type(AdvancementType.INVENTORY)
                .goal(SlimefunItems.ENERGY_REGULATOR)
                .register();

        electric.register();
    }
}

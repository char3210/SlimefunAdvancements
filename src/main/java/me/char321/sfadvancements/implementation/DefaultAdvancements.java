package me.char321.sfadvancements.implementation;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.char321.sfadvancements.api.AdvancementBuilder;
import me.char321.sfadvancements.api.AdvancementGroup;
import me.char321.sfadvancements.api.criteria.InteractCriterion;
import me.char321.sfadvancements.util.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DefaultAdvancements {
    static AdvancementGroup basic = new AdvancementGroup(new CustomItemStack(Material.SLIME_BALL,
            "&fBasic",
            "&7&oThe core spirit of Slimefun."));
    static AdvancementGroup electric = new AdvancementGroup(new CustomItemStack(Material.REDSTONE,
            "&eElectric",
            "&7&oThe center of civilization."));

    public static void registerDefaultAdvancements() {
        new AdvancementBuilder()
                .key(Utils.keyOf("interact"))
                .group(basic)
                .display(new CustomItemStack(Material.CRAFTING_TABLE,
                        "&aGetting Started",
                        "interact with a totem"))
                .name("&a[Getting Started]")
                .criteria(new InteractCriterion(
                        Utils.keyOf("interact"),
                        "interact",
                        1,
                        new ItemStack(Material.TOTEM_OF_UNDYING)
                ))
                .register();
        basic.register();

    }
}

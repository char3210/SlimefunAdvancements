package me.char321.sfadvancements.core;

import io.github.thebusybiscuit.slimefun4.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AdvancementsItemGroup extends FlexItemGroup {

    public AdvancementsItemGroup() {
        super(
                new NamespacedKey(SFAdvancements.instance(), "advancements"),
                new CustomItemStack(Material.FILLED_MAP, "&9Advancements"),
                0);
    }

    @Override
    public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideMode layout) {
        return true;
    }

    @Override
    public void open(Player p, PlayerProfile profile, SlimefunGuideMode layout) {
        SFAdvancements.getGuiManager().displayGUI(p);
    }
}

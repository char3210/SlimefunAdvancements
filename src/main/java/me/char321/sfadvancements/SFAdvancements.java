package me.char321.sfadvancements;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.char321.sfadvancements.api.AdvancementGroup;
import me.char321.sfadvancements.core.gui.AdvGUIManager;
import me.char321.sfadvancements.core.itemgroup.AdvancementsItemGroup;
import me.char321.sfadvancements.core.registry.AdvancementsRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class SFAdvancements extends JavaPlugin implements SlimefunAddon {
    private static SFAdvancements instance;
    private final AdvGUIManager guiManager = new AdvGUIManager();
    private final AdvancementsRegistry registry = new AdvancementsRegistry();

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(guiManager, this);
        new AdvancementsItemGroup().register(this);

        registerDefaultAdvancements();
    }


    @Override
    public void onDisable() {

    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return null;
    }

    public static SFAdvancements instance() {
        return instance;
    }

    public static AdvGUIManager getGuiManager() {
        return instance.guiManager;
    }

    public static AdvancementsRegistry getRegistry() {
        return instance.registry;
    }

    public static void info(Object msg) {
        instance.getLogger().info(msg.toString());
    }

    private void registerDefaultAdvancements() {

    }

}

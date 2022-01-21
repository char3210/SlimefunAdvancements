package me.char321.sfadvancements;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.char321.sfadvancements.core.AdvManager;
import me.char321.sfadvancements.core.command.SFACommand;
import me.char321.sfadvancements.core.tasks.AutoSaveTask;
import me.char321.sfadvancements.core.tasks.InventoryTask;
import me.char321.sfadvancements.core.gui.AdvGUIManager;
import me.char321.sfadvancements.core.AdvancementsItemGroup;
import me.char321.sfadvancements.core.registry.AdvancementsRegistry;
import me.char321.sfadvancements.implementation.DefaultAdvancements;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public final class SFAdvancements extends JavaPlugin implements SlimefunAddon {
    private static SFAdvancements instance;
    private final AdvManager advManager = new AdvManager();
    private final AdvGUIManager guiManager = new AdvGUIManager();
    private final AdvancementsRegistry registry = new AdvancementsRegistry();

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(guiManager, this);
        new AdvancementsItemGroup().register(this);

        DefaultAdvancements.registerDefaultAdvancements();

        getCommand("sfadvancements").setExecutor(new SFACommand(this));

        new InventoryTask().runTaskTimer(this, 10L, 10L);
        new AutoSaveTask().runTaskTimerAsynchronously(this, 6000L, 6000L);
    }


    @Override
    public void onDisable() {
        try {
            advManager.save();
        } catch (IOException e) {
            error("could not save advancements");
            e.printStackTrace();
        }
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

    public static AdvManager getAdvManager() {
        return instance.advManager;
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

    public static void warn(Object msg) {
        instance.getLogger().warning(msg.toString());
    }

    public static void error(Object msg) {
        instance.getLogger().severe(msg.toString());
    }

}

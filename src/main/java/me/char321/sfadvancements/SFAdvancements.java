package me.char321.sfadvancements;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;
import me.char321.sfadvancements.api.AdvancementBuilder;
import me.char321.sfadvancements.api.AdvancementGroup;
import me.char321.sfadvancements.api.criteria.CriteriaTypes;
import me.char321.sfadvancements.core.AdvManager;
import me.char321.sfadvancements.core.AdvancementsItemGroup;
import me.char321.sfadvancements.core.command.SFACommand;
import me.char321.sfadvancements.core.criteria.completer.DefaultCompleters;
import me.char321.sfadvancements.core.gui.AdvGUIManager;
import me.char321.sfadvancements.core.registry.AdvancementsRegistry;
import me.char321.sfadvancements.core.tasks.AutoSaveTask;
import me.char321.sfadvancements.util.ConfigUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SFAdvancements extends JavaPlugin implements SlimefunAddon {
    private static SFAdvancements instance;
    private final AdvManager advManager = new AdvManager();
    private final AdvGUIManager guiManager = new AdvGUIManager();
    private final AdvancementsRegistry registry = new AdvancementsRegistry();

    private FileConfiguration advancementConfig;
    private FileConfiguration groupConfig;

    @Override
    public void onEnable() {
        instance = this;

        info("Checking for updates...");
        autoUpdate();

        Bukkit.getPluginManager().registerEvents(guiManager, this);
        new AdvancementsItemGroup().register(this);

        DefaultCompleters.registerDefaultCompleters();
        CriteriaTypes.loadDefaultCriteria();

        getCommand("sfadvancements").setExecutor(new SFACommand(this));

        info("Starting auto-save task...");
        new AutoSaveTask().runTaskTimerAsynchronously(this, 6000L, 6000L);

        new Metrics(this, 14130);

        //allow other plugins to register their criteria first
        new BukkitRunnable() {
            @Override
            public void run() {
                info("Loading groups from config...");
                loadGroups();
                info("Loading advancements from config...");
                loadAdvancements();
            }
        }.runTaskLater(this, 0L);

    }

    @Override
    public void onDisable() {
        try {
            advManager.save();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, e, () -> "Could not save advancements");
        }
    }

    private void autoUpdate() {
        Config config = new Config(this);
        if (config.getBoolean("auto-update") && !getDescription().getVersion().contains("MODIFIED")) {
            GitHubBuildsUpdater updater = new GitHubBuildsUpdater(this, this.getFile(), "qwertyuioplkjhgfd/SlimefunAdvancements/main");
            updater.start();
        }
    }

    private void loadGroups() {
        File groupFile = new File("plugins/" + getName(), "groups.yml");
        if(!groupFile.exists()) {
            saveResource("groups.yml", false);
        }
        groupConfig = YamlConfiguration.loadConfiguration(groupFile);
        for (String key : groupConfig.getKeys(false)) {
            ItemStack display = ConfigUtils.getItem(groupConfig, key+".display");
            AdvancementGroup group = new AdvancementGroup(key, display);
            group.register();
        }
    }

    private void loadAdvancements() {
//        DefaultAdvancements.registerDefaultAdvancements();
        File advancementsFile = new File("plugins/" + getName(), "advancements.yml");
        if(!advancementsFile.exists()) {
            saveResource("advancements.yml", false);
        }
        advancementConfig = YamlConfiguration.loadConfiguration(advancementsFile);
        for (String key : advancementConfig.getKeys(false)) {
            AdvancementBuilder builder = AdvancementBuilder.loadFromConfig(key, advancementConfig.getConfigurationSection(key));
            if (builder != null) {
                builder.register();
            }
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

    public static Logger logger() {
        return instance.getLogger();
    }

    public static void info(String msg) {
        instance.getLogger().info(msg);
    }

    public static void warn(String msg) {
        instance.getLogger().warning(msg);
    }

    public static void error(String msg) {
        instance.getLogger().severe(msg);
    }

}

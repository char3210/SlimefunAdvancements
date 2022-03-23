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
import me.char321.sfadvancements.core.criteria.completer.CriterionCompleter;
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
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
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

    public boolean testing = false;

    public SFAdvancements() {

    }

    public SFAdvancements(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        testing = true;
    }

    @Override
    public void onEnable() {
        instance = this;

        autoUpdate();

        getCommand("sfadvancements").setExecutor(new SFACommand(this));

        // init gui
        Bukkit.getPluginManager().registerEvents(guiManager, this);

        // init sf
        new AdvancementsItemGroup().register(this);

        // init core
        DefaultCompleters.registerDefaultCompleters();
        CriteriaTypes.loadDefaultCriteria();

        info("Starting auto-save task...");
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new AutoSaveTask(), 6000L, 6000L);

        if (!testing) {
            new Metrics(this, 14130);
        }

        //allow other plugins to register their criteria completers
        info("Waiting for server start...");
        Bukkit.getScheduler().runTaskLater(this, () -> {
            info("Loading groups from config...");
            loadGroups();
            info("Loading advancements from config...");
            loadAdvancements();
        }, 0L);

    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        try {
            advManager.save();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, e, () -> "Could not save advancements");
        }
    }

    private void autoUpdate() {
        Config config = new Config(this);
        if (config.getBoolean("auto-update") && !getDescription().getVersion().contains("MODIFIED")) {
            info("Checking for updates...");
            GitHubBuildsUpdater updater = new GitHubBuildsUpdater(this, this.getFile(), "qwertyuioplkjhgfd/SlimefunAdvancements/main");
            updater.start();
        }
    }

    public void reload() {
        advManager.getPlayerMap().clear();
        registry.getAdvancements().clear();
        registry.getAdvancementGroups().clear();
        registry.getCompleters().values().forEach(CriterionCompleter::reload);

        loadGroups();
        loadAdvancements();
    }

    public void loadGroups() {
        File groupFile = new File(getDataFolder(), "groups.yml");
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

    public void loadAdvancements() {
        File advancementsFile = new File(getDataFolder(), "advancements.yml");
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

    public FileConfiguration getAdvancementConfig() {
        return advancementConfig;
    }

    public FileConfiguration getGroupsConfig() {
        return groupConfig;
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

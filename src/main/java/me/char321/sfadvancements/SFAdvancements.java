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
import me.char321.sfadvancements.util.Utils;
import me.char321.sfadvancements.vanilla.VanillaHook;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

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
    private final VanillaHook vanillaHook = new VanillaHook();

    private Config config;
    private YamlConfiguration advancementConfig;
    private YamlConfiguration groupConfig;

    private boolean testing = false;

    public SFAdvancements() {

    }

    public SFAdvancements(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        testing = true;
    }

    @Override
    public void onEnable() {
        instance = this;

        config = new Config(this);

        autoUpdate();

        getCommand("sfadvancements").setExecutor(new SFACommand(this));

        // init gui
        Bukkit.getPluginManager().registerEvents(guiManager, this);

        // init sf
        AdvancementsItemGroup.init(this);

        // init core
        DefaultCompleters.registerDefaultCompleters();
        CriteriaTypes.loadDefaultCriteria();

        info("Starting auto-save task...");
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new AutoSaveTask(), 6000L, 6000L);

        if (!testing) {
            Metrics metrics = new Metrics(this, 14130);
            metrics.addCustomChart(new SimplePie("AdvancementAPI enabled", () -> config.getBoolean("use-advancements-api") ? "true" : "false"));
        }

        //allow other plugins to register their criteria completers
        info("Waiting for server start...");
        Utils.runLater(() -> {
            info("Loading groups from config...");
            loadGroups();
            info("Loading advancements from config...");
            loadAdvancements();

            if (!testing && config.getBoolean("use-advancements-api")) {
                vanillaHook.init();
            }
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
        if (config.getBoolean("auto-update") && !getDescription().getVersion().contains("MODIFIED")) {
            info("Checking for updates...");
            GitHubBuildsUpdater updater = new GitHubBuildsUpdater(this, this.getFile(), "qwertyuioplkjhgfd/SlimefunAdvancements/main");
            updater.start();
        }
    }

    public void reload() {
        config.reload();
        advManager.getPlayerMap().clear();
        registry.getAdvancements().clear();
        registry.getAdvancementGroups().clear();
        registry.getCompleters().values().forEach(CriterionCompleter::reload);

        loadGroups();
        loadAdvancements();

        if (!testing && config.getBoolean("use-advancements-api")) {
            vanillaHook.reload();
        }
    }

    public void loadGroups() {
        File groupFile = new File(getDataFolder(), "groups.yml");
        if (!groupFile.exists()) {
            saveResource("groups.yml", false);
        }
        groupConfig = YamlConfiguration.loadConfiguration(groupFile);
        for (String key : groupConfig.getKeys(false)) {
            String background = groupConfig.getString(key + ".background", "BEDROCK");
            ItemStack display = ConfigUtils.getItem(groupConfig, key + ".display");
            AdvancementGroup group = new AdvancementGroup(key, display, background);
            group.register();
        }
    }

    public void loadAdvancements() {
        File advancementsFile = new File(getDataFolder(), "advancements.yml");
        if (!advancementsFile.exists()) {
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

    public static VanillaHook getVanillaHook() {
        return instance.vanillaHook;
    }

    public static Config getMainConfig() {
        return instance.config;
    }

    public YamlConfiguration getAdvancementConfig() {
        return advancementConfig;
    }

    public YamlConfiguration getGroupsConfig() {
        return groupConfig;
    }

    public boolean isTesting() {
        return testing;
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
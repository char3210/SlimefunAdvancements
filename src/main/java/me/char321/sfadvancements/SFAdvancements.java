package me.char321.sfadvancements;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import me.char321.sfadvancements.api.AdvancementBuilder;
import me.char321.sfadvancements.api.AdvancementGroup;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.api.criteria.InteractCriterion;
import me.char321.sfadvancements.api.criteria.InventoryCriterion;
import me.char321.sfadvancements.api.criteria.PlaceCriterion;
import me.char321.sfadvancements.api.criteria.ResearchCriterion;
import me.char321.sfadvancements.core.AdvManager;
import me.char321.sfadvancements.core.AdvancementsItemGroup;
import me.char321.sfadvancements.core.command.SFACommand;
import me.char321.sfadvancements.core.criteria.completer.DefaultCompleters;
import me.char321.sfadvancements.core.gui.AdvGUIManager;
import me.char321.sfadvancements.core.registry.AdvancementsRegistry;
import me.char321.sfadvancements.core.tasks.AutoSaveTask;
import me.char321.sfadvancements.implementation.DefaultAdvancements;
import me.char321.sfadvancements.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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

        Bukkit.getPluginManager().registerEvents(guiManager, this);
        new AdvancementsItemGroup().register(this);

        DefaultCompleters.registerDefaultCompleters();

        loadGroups();
        loadAdvancements();

        getCommand("sfadvancements").setExecutor(new SFACommand(this));

        new AutoSaveTask().runTaskTimerAsynchronously(this, 6000L, 6000L);
    }

    @Override
    public void onDisable() {
        try {
            advManager.save();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, e, () -> "could not save advancements");
        }
    }

    private void loadGroups() {
        groupConfig = YamlConfiguration.loadConfiguration(new File("plugins/" + getName(), "groups.yml"));
        for (String key : groupConfig.getKeys(false)) {
            ItemStack display = groupConfig.getItemStack(key+".display");
            AdvancementGroup group = new AdvancementGroup(key, display);
            group.register();
        }
    }

    private void loadAdvancements() {
        DefaultAdvancements.registerDefaultAdvancements();

        advancementConfig = YamlConfiguration.loadConfiguration(new File("plugins/" + getName(), "advancements.yml"));
        for (String key : advancementConfig.getKeys(false)) {
            AdvancementBuilder builder = new AdvancementBuilder();
            builder.key(Utils.keyOf(key));
            builder.group(advancementConfig.getString(key+".group"));
            builder.display(advancementConfig.getItemStack(key+".display"));
            builder.name(advancementConfig.getString(key+".name"));
            builder.criteria(loadCriteria(key).toArray(new Criterion[0]));
            builder.register();
        }
    }

    private List<Criterion> loadCriteria(String key) {
        List<Criterion> res = new ArrayList<>();
        ConfigurationSection criteriaSection = advancementConfig.getConfigurationSection(key + ".criteria");
        if(criteriaSection == null) {
            info("advancement " + key + " must specify criteria!");
            return Collections.emptyList();
        }

        for (String id : criteriaSection.getKeys(false)) {
            ConfigurationSection cripath = criteriaSection.getConfigurationSection(id);
//            String cripath = key + ".criteria." + id;
            String type = cripath.getString("type");
            if(type == null) {
                warn("You must specify a typE!!!!!!!111 for criterion " + id + " in advancements.yml");
                continue;
            }
            Criterion criterion;
            switch(type.toLowerCase(Locale.ROOT)) {
                case "interact":
                    int amount = cripath.getInt("amount");
                    if(amount == 0) {
                        amount = 1;
                    }
                    ItemStack item = cripath.getItemStack("item");
                    if(item == null) {
                        warn("unknown item for interact criterion " + id);
                        continue;
                    }
                    criterion = new InteractCriterion(id, amount, item);
                    break;
                case "inventory":
                    item = cripath.getItemStack("item");
                    if(item == null) {
                        warn("unknown item for inventory criterion " + id);
                        continue;
                    }
                    criterion = new InventoryCriterion(id, item);
                    break;
                case "place":
                    item = cripath.getItemStack("item");
                    if(item == null) {
                        warn("unknown item for place criterion " + id);
                        continue;
                    }
                    amount = cripath.getInt("amount");
                    if(amount == 0) {
                        amount = 1;
                    }
                    criterion = new PlaceCriterion(id, amount, item);
                    break;
                case "research":
                    String research = cripath.getString("research");
                    if(research == null) {
                        warn("specify a research for criterion " + id);
                        continue;
                    }
                    criterion = new ResearchCriterion(id, NamespacedKey.fromString(research));
                    break;
                default:
                    warn("unknown criterion type: " + type);
                    continue;
            }
            criterion.setAdvancement(Utils.keyOf(key));
            res.add(criterion);
        }
        return res;
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

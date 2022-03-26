package me.char321.sfadvancements.core.command;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class ImportCommand implements SubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " import <plugin>");
            return false;
        }

        Plugin pl = Bukkit.getPluginManager().getPlugin(args[1]);
        if (pl == null) {
            sender.sendMessage(ChatColor.RED + "Plugin " + args[1] + " not found.");
            return false;
        }

        sender.sendMessage("Importing advancements from " + pl.getName());

        InputStream advInputStream = pl.getResource("sfadvancements.yml");
        if (advInputStream == null) {
            sender.sendMessage(ChatColor.RED + "Plugin " + pl.getName() + " does not have default advancements.");
            return false;
        }

        InputStream groupInputStream = pl.getResource("sfagroups.yml");
        if (groupInputStream == null) {
            sender.sendMessage(ChatColor.YELLOW + "Plugin " + pl.getName() + " does not specify default groups.");
            sender.sendMessage(ChatColor.YELLOW + "Any imported advancements may have unknown groups.");
        }

        saveBackups();
        importGroups(pl, groupInputStream);
        importAdvancements(pl, advInputStream);

        sender.sendMessage("Done! Restart the server or use /sfa reload to apply the changes.");
        return true;
    }

    private void saveBackups() {
        File dataFolder = SFAdvancements.instance().getDataFolder();
        File backupFolder = new File(dataFolder, "backups");
        if (!backupFolder.exists()) {
            backupFolder.mkdirs();
        }
        if (!backupFolder.isDirectory()) {
            throw new IllegalStateException("File " + backupFolder + " is not a directory");
        }

        File groupFile = new File(dataFolder, "groups.yml");
        File advFile = new File(dataFolder, "advancements.yml");

        String now = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        int index = 0;
        File groupFileOut;
        File advFileOut;
        do {
            index++;
            groupFileOut = new File(backupFolder, "groups-backup-" + now + " (" + index + ").yml");
            advFileOut = new File(backupFolder, "advancements-backup-" + now + " (" + index + ").yml");
        } while (groupFileOut.exists() || advFileOut.exists());

        try {
            if (groupFile.exists()) {
                Files.copy(groupFile.toPath(), groupFileOut.toPath());
            }
            if (advFile.exists()) {
                Files.copy(advFile.toPath(), advFileOut.toPath());
            }
        } catch (IOException ex) {
            SFAdvancements.logger().log(Level.SEVERE, ex, () -> "Error while saving advancement backups");
        }
    }

    private void importGroups(Plugin pl, InputStream groupInputStream) {
        if (groupInputStream == null) {
            return;
        }

        File outfile = new File(SFAdvancements.instance().getDataFolder(), "groups.yml");
        BufferedReader reader = new BufferedReader(new InputStreamReader(groupInputStream));
        YamlConfiguration config = YamlConfiguration.loadConfiguration(reader);
        YamlConfiguration original = SFAdvancements.instance().getGroupsConfig();

        Set<String> keys = config.getKeys(false);
        for (String key : keys) {
            if (original.isSet(key)) {
                SFAdvancements.info("group key " + key + " already exists in original; not replacing");
            } else {
                original.set(key, config.get(key));
            }
        }
        try {
            original.save(outfile);
        } catch (IOException ex) {
            SFAdvancements.logger().log(Level.SEVERE, ex, () -> "Could not save groups");
        }
    }

    private void importAdvancements(Plugin pl, InputStream advInputStream) {
        File outfile = new File(SFAdvancements.instance().getDataFolder(), "advancements.yml");
        BufferedReader reader = new BufferedReader(new InputStreamReader(advInputStream));
        YamlConfiguration config = YamlConfiguration.loadConfiguration(reader);
        YamlConfiguration original = SFAdvancements.instance().getAdvancementConfig();

        Set<String> keys = config.getKeys(false);
        for (String key : keys) {
            if (original.isSet(key)) {
                SFAdvancements.info("advancement key " + key + " already exists in original; not replacing");
            } else {
                original.set(key, config.get(key));
            }
        }
        try {
            original.save(outfile);
        } catch (IOException ex) {
            SFAdvancements.logger().log(Level.SEVERE, ex, () -> "Could not save advancements");
        }
    }

    @Nonnull
    @Override
    public String getCommandName() {
        return "import";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> res = new ArrayList<>();
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        for (Plugin plugin : plugins) {
            String name = plugin.getName();
            if (name.contains(args[1])) {
                res.add(name);
            }
        }
        return res;
    }
}

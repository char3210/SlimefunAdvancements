package me.char321.sfadvancements.core.command;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class ReloadCommand implements SubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command command, String label, String[] args) {
        try {
            SFAdvancements.getAdvManager().save();
        } catch (IOException e) {
            sender.sendMessage(ChatColor.RED + "Error while saving advancements! Check the console for details. Reload cancelled.");
            SFAdvancements.logger().log(Level.SEVERE, e, () -> "Could not save advancements while reloading");
            return false;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (SFAdvancements.getGuiManager().isOpen(player)) {
                player.closeInventory();
            }
        }

        SFAdvancements.instance().reload();

        sender.sendMessage("Successfully reloaded!");
        return false;
    }

    @Nonnull
    @Override
    public String getCommandName() {
        return "reload";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}

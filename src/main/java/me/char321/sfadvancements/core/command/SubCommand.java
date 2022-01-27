package me.char321.sfadvancements.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    boolean onExecute(CommandSender sender, Command command, String label, String[] args);
    String getCommandName();
    List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);
}

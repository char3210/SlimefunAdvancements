package me.char321.sfadvancements.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {
    abstract boolean onExecute(CommandSender sender, Command command, String label, String[] args);
    abstract String getCommandName();
    abstract List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);
}

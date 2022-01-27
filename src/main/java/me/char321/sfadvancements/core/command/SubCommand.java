package me.char321.sfadvancements.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public interface SubCommand {
    boolean onExecute(CommandSender sender, Command command, String label, String[] args);
    @Nonnull
    String getCommandName();
    List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);
}

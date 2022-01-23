package me.char321.sfadvancements.core.command;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.List;

public class SaveCommand extends SubCommand {

    @Override
    boolean onExecute(CommandSender sender, Command command, String label, String[] args) {
        try {
            SFAdvancements.getAdvManager().save();
            sender.sendMessage("Successfully saved advancements.");
            return true;
        } catch(IOException e) {
            sender.sendMessage("An error occured while saving advancements!");
            sender.sendMessage("Check the console for details.");
            SFAdvancements.warn(e);
            return false;
        }
    }

    @Override
    String getCommandName() {
        return "save";
    }

    @Override
    List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}

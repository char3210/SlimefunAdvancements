package me.char321.sfadvancements.core.command;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.core.criteria.progress.PlayerProgress;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RevokeCommand implements SubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <player> <advancement>");
            return false;
        }

        Player p = Bukkit.getPlayer(args[1]);
        if (p == null) {
            sender.sendMessage(ChatColor.RED + "Could not find player " + args[1]);
            return false;
        }

        PlayerProgress progress = SFAdvancements.getAdvManager().getProgress(p);
        if (args[2].equals("*") || args[2].equals("all")) {
            for (NamespacedKey adv : progress.getCompletedAdvancements()) {
                progress.revokeAdvancement(adv);
            }
            sender.sendMessage("Successfully revoked all achievements!");
            return true;
        }

        if (!progress.revokeAdvancement(NamespacedKey.fromString(args[2]))) {
            sender.sendMessage(ChatColor.RED + "Could not revoke advancement " + args[2] + " from player " + args[1]);
            return false;
        } else {
            sender.sendMessage("Successfully revoked!");
            return true;
        }
    }

    @Override
    public String getCommandName() {
        return "revoke";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            List<String> res = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().contains(args[1])) {
                    res.add(p.getName());
                }
            }
            return res;
        } else if (args.length == 3) {
            Player p = Bukkit.getPlayer(args[1]);
            if (p != null) {
                List<String> res = new ArrayList<>();
                res.add("*");
                res.add("all");
                for (NamespacedKey key : SFAdvancements.getAdvManager().getProgress(p).getCompletedAdvancements()) {
                    res.add(key.toString());
                }
                return res;
            }
        }
        return Collections.emptyList();
    }

}

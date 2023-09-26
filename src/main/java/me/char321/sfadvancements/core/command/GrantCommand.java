package me.char321.sfadvancements.core.command;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrantCommand implements SubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " grant <player> <advancement>");
            return false;
        }

        Player p = Bukkit.getPlayer(args[1]);
        if (p == null) {
            sender.sendMessage(ChatColor.RED + "Could not find player " + args[1]);
            return false;
        }

        if (args[2].equals("*") || args[2].equals("all")) {
            for (Advancement adv : SFAdvancements.getRegistry().getAdvancements().values()) {
                adv.onComplete(p);
            }
            sender.sendMessage("Successfully granted all advancements!");
            return true;
        }

        Advancement adv = SFAdvancements.getRegistry().getAdvancement(NamespacedKey.fromString(args[2]));
        if (adv == null) {
            sender.sendMessage(ChatColor.RED + "Could not find advancement " + args[2]);
            return false;
        }

        adv.onComplete(p);
        sender.sendMessage("Successfully granted advancement " + adv.getKey() + " to player " + p.getName());
        return true;
    }

    @Override
    public @Nonnull String getCommandName() {
        return "grant";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) { //if only brigadier existed in spigot api
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
                for (Advancement adv : SFAdvancements.getRegistry().getAdvancements().values()) {
                    if (!SFAdvancements.getAdvManager().isCompleted(p, adv)) {
                        String s = adv.getKey().toString();
                        if(s.contains(args[2])) {
                            res.add(s);
                        }
                    }
                }
                return res;
            }
        }
        return Collections.emptyList();
    }
}

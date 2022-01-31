package me.char321.sfadvancements.core.command;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DumpItemCommand implements SubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player p)) {
            sender.sendMessage("you must be a player to execute this command");
            return false;
        }

        sender.sendMessage("Representing the item in your hand in yml...");
        ItemStack item = p.getInventory().getItemInMainHand();
        SFAdvancements.info("Representing " + item);

        if(!item.hasItemMeta()) {
            SFAdvancements.info("This item can be represented with just the string \n" + item.getType().name());
        }

        ItemMeta im = item.getItemMeta();

        String type = item.getType().name();

        Optional<String> itemData = Slimefun.getItemDataService().getItemData(im);
        if (itemData.isPresent()) {
            String id = itemData.get();
            if (SlimefunUtils.isItemSimilar(item, SlimefunItem.getById(id).getItem(), true)) {
                SFAdvancements.info("This item can be represented with just the string \n" + id);
            }
            type = id;
        }

        StringBuilder representation = new StringBuilder();
        representation.append("type: ").append(type).append("\n");
        representation.append("name: ").append(ItemUtils.getItemName(item).replace(ChatColor.COLOR_CHAR, '&').replaceAll("[\\[\\]]", "")).append("\n");
        if(im.hasLore()) {
            representation.append("lore: ").append("\n");
            for (String s : im.getLore()) {
                representation.append("  - ").append(s.replace(ChatColor.COLOR_CHAR, '&')).append("\n");
            }
        }
        SFAdvancements.info("If you only care about looks, you can represent the item with \n" + representation);

        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("item", item);
        SFAdvancements.info("Full yml representation: \n" + configuration.saveToString());

        sender.sendMessage("Done! Check the console for details.");
        return true;
    }

    @Nonnull
    @Override
    public String getCommandName() {
        return "dumpitem";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}

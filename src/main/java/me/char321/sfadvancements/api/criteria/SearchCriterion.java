package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.util.ConfigUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class SearchCriterion extends Criterion {

    private final String search;

    public static SearchCriterion loadFromConfig(ConfigurationSection config) {
        String id = config.getName();

        String name = config.getString("name");
        if(name == null) {
            name = id;
        }

        name = ChatColor.translateAlternateColorCodes('&', name);

        int amount = config.getInt("amount");
        if (amount == 0) {
            amount = 1;
        }

        String search = config.getString("search");
        if (search == null) {
            SFAdvancements.warn("search not provided for " + id);
            return null;
        }

        return new SearchCriterion(id, amount, name, search);
    }


    public SearchCriterion(String id, int count, String name, String search) {
        super(id, count, name);
        this.search = search;
    }

    public String getSearch() {
        return search;
    }


}

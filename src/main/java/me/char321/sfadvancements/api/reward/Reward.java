package me.char321.sfadvancements.api.reward;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface Reward {
    void give(Player p);
}

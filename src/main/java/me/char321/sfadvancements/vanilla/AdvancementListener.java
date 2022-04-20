package me.char321.sfadvancements.vanilla;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

/**
 * sync progress if someone uses /advancement grant ...
 *
 */
public class AdvancementListener implements Listener {
    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent e) {
        if (!Utils.keyIsSFA(e.getAdvancement().getKey())) return;

        Advancement advancement = Utils.fromKey(e.getAdvancement().getKey());
        if (advancement == null) return;

        Player player = e.getPlayer();
        if (SFAdvancements.getAdvManager().isCompleted(player, advancement)) return;

        advancement.complete(player);
    }
}
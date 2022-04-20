package me.char321.sfadvancements.vanilla;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (SFAdvancements.getMainConfig().getBoolean("use-advancements-api")){
            SFAdvancements.getVanillaHook().syncProgress(e.getPlayer());
        }
    }
}

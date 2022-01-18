package me.char321.sfadvancements.core.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdvGUIManager implements Listener {
    private Map<UUID, OpenGUI> guis = new HashMap<>();

    public void displayGUI(Player p) {
        OpenGUI gui = getByPlayer(p);
        p.openInventory(gui.getInventory());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        OpenGUI openGUI = getByPlayer((Player) e.getWhoClicked());
        if(e.getInventory().equals(openGUI.getInventory())) {
            openGUI.click(e.getRawSlot());
            e.setCancelled(true);
        }
    }

    public OpenGUI getByPlayer(Player p) {
        UUID uuid = p.getUniqueId();
        if(!guis.containsKey(uuid)) {
            guis.put(uuid, new OpenGUI());
        }
        return guis.get(uuid);
    }
}

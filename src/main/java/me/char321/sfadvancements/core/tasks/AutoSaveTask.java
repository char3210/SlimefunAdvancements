package me.char321.sfadvancements.core.tasks;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class AutoSaveTask extends BukkitRunnable {
    @Override
    public void run() {
        SFAdvancements.info("Auto-saving advancements...");
        try {
            SFAdvancements.getAdvManager().save();
            SFAdvancements.info("Successfully saved!");
        } catch (IOException e) {
            SFAdvancements.error("Could not auto-save advancements!");
            e.printStackTrace();
        }
    }
}

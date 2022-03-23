package me.char321.sfadvancements.core.tasks;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.logging.Level;

public class AutoSaveTask implements Runnable {
    @Override
    public void run() {
        try {
            SFAdvancements.getAdvManager().save();
        } catch (IOException e) {
            SFAdvancements.logger().log(Level.SEVERE, e, () -> "Could not auto-save advancements!");
        }
    }
}

package me.char321.sfadvancements.core.tasks;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateAdvancementsTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Advancement adv : SFAdvancements.getRegistry().getAdvancements().values()) {
            adv.updateCriteria();
        }
    }
}

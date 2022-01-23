package me.char321.sfadvancements.api.criteria;

import com.google.gson.JsonObject;
import me.char321.sfadvancements.api.Advancement;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * that means this also has to be immutable
 */
public abstract class Criterion {
    protected NamespacedKey advancement;

    public Criterion(NamespacedKey adv) {
        this.advancement = adv;
    }

    public abstract JsonObject save(Player p);

    public void update() {

    }

    public NamespacedKey getAdvancement() {
        return advancement;
    }

}

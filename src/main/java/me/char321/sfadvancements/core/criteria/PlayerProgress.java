package me.char321.sfadvancements.core.criteria;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.core.criteria.progress.AdvancementProgress;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * a per-player object that stores their advancement progress <br>
 *
 * json <br>
 *
 * {@code
 * {
 *      "sfadvancements:hi" : {
 *          "done": false
 *          "criteria": {
 *              "eaj" : 1
 *              "ejfiow" : false
 *              "fioewjo" :
 *          }
 *      }
 * }
 * }
 *
 */
public class PlayerProgress {
    private UUID player;
    private Map<NamespacedKey, AdvancementProgress> progress;

    public static PlayerProgress get(Player player) {
        return load(player.getUniqueId());
    }

    public static PlayerProgress get(UUID player) {
        File f = new File("plugins/" + SFAdvancements.instance().getName() + "/advancements", player.toString()+".json");
        if(f.exists()) {
            try {
                JsonArray arr = JsonParser.parseReader(new BufferedReader(new FileReader(f, StandardCharsets.UTF_8))).getAsJsonArray();
                Set<NamespacedKey> advancements = new HashSet<>();
                for (JsonElement element : arr) {
                    advancements.add(NamespacedKey.fromString(element.getAsString()));
                }

            } catch (IOException e) {
                SFAdvancements.info("error reading file: " + e);
            }
        } else {
            return new PlayerProgress();
        }
    }
}

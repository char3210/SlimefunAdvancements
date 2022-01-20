package me.char321.sfadvancements.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AdvManager {
    private final Map<UUID, Set<NamespacedKey>> playerMap = new HashMap<>();

    public void complete(Player p, Advancement advancement) {
        complete(p.getUniqueId(), advancement);
    }

    public void complete(UUID uuid, Advancement advancement) {
        if(!playerMap.containsKey(uuid)) {
            loadPlayer(uuid);
        }
        Set<NamespacedKey> advancements = playerMap.get(uuid);
        advancements.add(advancement.getKey());
    }

    private void loadPlayer(UUID uuid) {
        File f = new File("plugins/" + SFAdvancements.instance().getName() + "/advancements", uuid.toString()+".json");
        if(f.exists()) {
            SFAdvancements.info("rEEE");
            try {
                JsonArray arr = JsonParser.parseReader(new BufferedReader(new FileReader(f, StandardCharsets.UTF_8))).getAsJsonArray();
                Set<NamespacedKey> advancements = new HashSet<>();
                for (JsonElement element : arr) {
                    advancements.add(NamespacedKey.fromString(element.getAsString()));
                }
                this.playerMap.put(uuid, advancements);
            } catch (IOException e) {
                SFAdvancements.info("error reading file: " + e);
            }
        } else {
            this.playerMap.put(uuid, new HashSet<>());
        }
    }

    public boolean isCompleted(Player player, Advancement advancement) {
        return isCompleted(player.getUniqueId(), advancement);
    }

    public boolean isCompleted(UUID player, Advancement advancement) {
        if(!playerMap.containsKey(player)) {
            loadPlayer(player);
        }
        Set<NamespacedKey> advancements = playerMap.get(player);
        return advancements.contains(advancement.getKey());

    }

    public void save() throws IOException {
        for (Map.Entry<UUID, Set<NamespacedKey>> entry : playerMap.entrySet()) {
            File f = new File("plugins/" + SFAdvancements.instance().getName() + "/advancements", entry.getKey().toString()+".json");
            f.mkdirs();
            //this is probably bad
            f.delete();
            f.createNewFile();
            JsonWriter writer = new JsonWriter(new BufferedWriter(new FileWriter(f)));
            writer.beginArray();
            for(NamespacedKey key : entry.getValue()) {
                writer.value(key.toString());
            }
            writer.endArray();
            writer.close();
        }
    }
}

package me.char321.sfadvancements.vanilla;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.api.AdvancementGroup;
import me.char321.sfadvancements.core.criteria.progress.PlayerProgress;
import me.char321.sfadvancements.util.Utils;
import net.roxeez.advancement.AdvancementCreator;
import net.roxeez.advancement.display.BackgroundType;
import net.roxeez.advancement.display.Icon;
import net.roxeez.advancement.trigger.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.stream.Collectors;

public class VanillaHook {
    private SFAdvancements plugin;
    private net.roxeez.advancement.AdvancementManager vanillaManager;

    public VanillaHook(SFAdvancements plugin) {
        this.plugin = plugin;
    }

    public void init() {
        vanillaManager = new net.roxeez.advancement.AdvancementManager(plugin);
        for (AdvancementGroup group : SFAdvancements.getRegistry().getAdvancementGroups()) {
            vanillaManager.register(context -> {
                net.roxeez.advancement.Advancement vadvancement = new net.roxeez.advancement.Advancement(Utils.keyOf(group.getId()));

                vadvancement.setDisplay(display -> {
                    ItemStack item = group.getDisplayItem();
                    ItemMeta meta = item.getItemMeta();
                    display.setTitle(meta.getDisplayName());
                    display.setDescription(String.join("", meta.getLore()));
                    display.setIcon(new Icon(item));
                    display.setBackground(BackgroundType.BEDROCK);
                });

                vadvancement.addCriteria("impossible", TriggerType.IMPOSSIBLE, a -> {});

                return vadvancement;
            });
        }

        for (Map.Entry<NamespacedKey, Advancement> entry : SFAdvancements.getRegistry().getAdvancements().entrySet()) {
            Advancement advancement = entry.getValue();
            vanillaManager.register(context -> {
                net.roxeez.advancement.Advancement vadvancement = new net.roxeez.advancement.Advancement(entry.getKey());

                vadvancement.setDisplay(display -> {
                    ItemStack item = advancement.getDisplay();
                    display.setTitle(advancement.getName().replaceAll("[\\[\\]]", ""));
                    display.setDescription(advancement.getDescription());
                    display.setIcon(new Icon(item));
                });

                vadvancement.setParent(Utils.keyOf(advancement.getGroup().getId()));
                vadvancement.addCriteria("impossible", TriggerType.IMPOSSIBLE, a -> {});

                return vadvancement;
            });
        }

        vanillaManager.createAll(false);

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), SFAdvancements.instance());
    }

    public void syncProgress(Player p) {
        PlayerProgress progress = SFAdvancements.getAdvManager().getProgress(p);
        for (Advancement adv : SFAdvancements.getRegistry().getAdvancements().values()) {
            if (progress.isCompleted(adv.getKey())) {
                complete(p, adv.getKey());
            } else {
                revoke(p, adv.getKey());
            }
        }
    }

    public void complete(Player p, NamespacedKey key) {
        org.bukkit.advancement.Advancement advancement = Bukkit.getAdvancement(key);
        if (advancement == null) {
            SFAdvancements.warn("Tried to complete unregistered advancement " + key);
            return;
        }
        Utils.runSync(() -> p.getAdvancementProgress(advancement).awardCriteria("impossible"));
    }

    public void revoke(Player p, NamespacedKey key) {
        org.bukkit.advancement.Advancement advancement = Bukkit.getAdvancement(key);
        if (advancement == null) {
            SFAdvancements.warn("Tried to revoke unregistered advancement " + key);
            return;
        }
        Utils.runSync(() -> p.getAdvancementProgress(advancement).revokeCriteria("impossible"));

    }
}

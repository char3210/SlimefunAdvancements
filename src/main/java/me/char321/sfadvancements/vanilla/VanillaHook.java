package me.char321.sfadvancements.vanilla;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.api.AdvancementGroup;
import me.char321.sfadvancements.core.criteria.progress.PlayerProgress;
import me.char321.sfadvancements.util.Utils;
import net.roxeez.advancement.AdvancementCreator;
import net.roxeez.advancement.AdvancementManager;
import net.roxeez.advancement.display.BackgroundType;
import net.roxeez.advancement.display.Icon;
import net.roxeez.advancement.trigger.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Map;

public class VanillaHook {
    private AdvancementManager vanillaManager;
    private boolean initialized = false;

    public void init() {
        if (initialized) return;
        initialized = true;

        this.vanillaManager = new AdvancementManager(SFAdvancements.instance());

        Utils.listen(new PlayerJoinListener());
        Utils.listen(new AdvancementListener());
        reload();
    }

    public void reload() {
        if (!initialized) {
            init();
        }

        vanillaManager.clearAdvancements();
        registerGroups(vanillaManager);
        registerAdvancements(vanillaManager);
        vanillaManager.createAll(true);

        for (Player p : Bukkit.getOnlinePlayers()) {
            syncProgress(p);
        }
    }

    private static void registerGroups(AdvancementManager manager) {
        for (AdvancementGroup group : SFAdvancements.getRegistry().getAdvancementGroups()) {
            manager.register(context -> {
                net.roxeez.advancement.Advancement vadvancement = new net.roxeez.advancement.Advancement(Utils.keyOf(group.getId()));

                vadvancement.setDisplay(display -> {
                    ItemStack item = group.getDisplayItem();
                    ItemMeta meta = item.getItemMeta();
                    display.setTitle(meta.getDisplayName());
                    display.setDescription(String.join("\n", meta.getLore()));
                    display.setIcon(new Icon(item));
                    display.setBackground(BackgroundType.BEDROCK);
                });

                vadvancement.addCriteria("impossible", TriggerType.IMPOSSIBLE, a -> {});

                return vadvancement;
            });
        }
    }

    private static void registerAdvancements(AdvancementManager manager) {
        for (Map.Entry<NamespacedKey, Advancement> entry : SFAdvancements.getRegistry().getAdvancements().entrySet()) {
            registerAdvancement(manager, entry.getValue());
        }
    }

    private static void registerAdvancement(AdvancementManager manager, Advancement advancement) {
        if (manager == null) return;
        if (advancement == null) return;

        if (manager.getAdvancements().stream().anyMatch(vadv -> vadv.getKey().equals(advancement.getKey()))) return;

        if (manager.getAdvancements().stream().noneMatch(vadv -> vadv.getKey().equals(advancement.getParent()))) {
            Advancement parent = Utils.fromKey(advancement.getParent());
            if (parent != null) {
                registerAdvancement(manager, parent);
            }
        }

        manager.register(context -> {
            net.roxeez.advancement.Advancement vadvancement = new net.roxeez.advancement.Advancement(advancement.getKey());

            vadvancement.setDisplay(display -> {
                ItemStack item = advancement.getDisplay();
                ItemMeta meta = item.getItemMeta();
                display.setTitle(meta.getDisplayName());
                display.setDescription(String.join("\n", meta.getLore()));
                display.setIcon(new Icon(item));
            });

            vadvancement.setParent(advancement.getParent());
            vadvancement.addCriteria("impossible", TriggerType.IMPOSSIBLE, a -> {});

            return vadvancement;
        });
    }

    public void syncProgress(Player p) {
        for (Advancement adv : SFAdvancements.getRegistry().getAdvancements().values()) {
            if (SFAdvancements.getAdvManager().isCompleted(p, adv)) {
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

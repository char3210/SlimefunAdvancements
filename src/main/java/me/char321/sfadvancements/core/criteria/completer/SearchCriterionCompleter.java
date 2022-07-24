package me.char321.sfadvancements.core.criteria.completer;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.GuideHistory;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.api.criteria.SearchCriterion;
import me.char321.sfadvancements.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCriterionCompleter implements CriterionCompleter {
    private final Map<String, List<SearchCriterion>> criteria = new HashMap<>();


    public SearchCriterionCompleter() {
        Field queueField;
        Method getIndexedObject;
        try {
            queueField = GuideHistory.class.getDeclaredField("queue");
            queueField.setAccessible(true);
            getIndexedObject = Class.forName("io.github.thebusybiscuit.slimefun4.core.guide.GuideEntry").getDeclaredMethod("getIndexedObject");
            getIndexedObject.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return;
        }

        Bukkit.getScheduler().runTaskTimer(SFAdvancements.instance(), () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                PlayerProfile.get(onlinePlayer, profile -> {
                    try {
                        Deque<?> queue = (Deque<?>) queueField.get(profile.getGuideHistory());
                        if (!queue.isEmpty()) {
                            Object str = getIndexedObject.invoke(queue.getLast());
                            if (str instanceof String) {
                                Utils.runSync(() -> onSearch(onlinePlayer, (String)str));
                            }
                        }
                    } catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 10, 10);
    }

    private void onSearch(Player player, String str) { //TODO make not trigger multiple times for one search
        List<SearchCriterion> searchCriteria = criteria.get(str);
        if (searchCriteria == null) return;

        for (SearchCriterion criterion : searchCriteria) {
            criterion.perform(player);
        }
    }

    @Override
    public void register(Criterion criterion) {
        if (!(criterion instanceof SearchCriterion)) {
            throw new IllegalArgumentException("criterion must be an " + getCriterionClass().getName());
        }

        SearchCriterion criterion1 = (SearchCriterion) criterion;
        criteria.computeIfAbsent(criterion1.getSearch(), k -> new ArrayList<>()).add(criterion1);
    }

    @Override
    public Class<? extends Criterion> getCriterionClass() {
        return SearchCriterion.class;
    }

    @Override
    public void reload() {
        criteria.clear();
    }
}

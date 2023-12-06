package me.char321.sfadvancements.core.criteria.completer;

import me.char321.sfadvancements.SFAdvancements;

public class DefaultCompleters {

    private DefaultCompleters() {

    }

    public static void registerDefaultCompleters() {
        new InteractCriterionCompleter().register();
        new InventoryCriterionCompleter().register();
        new PlaceCriterionCompleter().register();
        new ResearchCriterionCompleter().register();
        new MultiBlockCriterionCompleter().register();
        new ConsumeCriterionCompleter().register();
        new MobKillCriterionCompleter().register();
        new SearchCriterionCompleter().register();
        new BlockBreakCriterionCompleter().register();

        if (SFAdvancements.instance().isMultiBlockCraftEvent()) {
            new MultiBlockCraftCriterionCompleter().register();
        }
    }
}

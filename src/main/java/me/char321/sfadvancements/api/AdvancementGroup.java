package me.char321.sfadvancements.api;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdvancementGroup {
    private final ItemStack display;
    private List<Advancement> advancements = new ArrayList<>();
    private String id;
    private String background;

    public AdvancementGroup(String id, ItemStack display) {
        this(id, display, "BEDROCK");
    }

    public AdvancementGroup(String id, ItemStack display, String background) {
        this.id = id;
        this.background = background;
        this.display = display;
    }

    public void register() {
        SFAdvancements.getRegistry().getAdvancementGroups().add(this);
    }

    public ItemStack getDisplayItem() {
        return display;
    }

    /**
     * gets an immutable view of the advancements
     * @return list of all advancements in this group
     */
    public List<Advancement> getAdvancements() {
        return Collections.unmodifiableList(advancements);
    }

    /**
     * @param player the player
     * @return the list of advancements in this group that can be seen
     */
    public List<Advancement> getVisibleAdvancements(UUID player) {
        return advancements.stream().filter(a -> !a.isHidden() || SFAdvancements.getAdvManager().isCompleted(player, a)).collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public String getBackground() {
        return background;
    }

    public void addAdvancement(Advancement advancement) {
        advancements.add(advancement);
    }
}

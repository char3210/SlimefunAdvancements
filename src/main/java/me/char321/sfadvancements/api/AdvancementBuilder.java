package me.char321.sfadvancements.api;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class AdvancementBuilder {
    private NamespacedKey key;
    private AdvancementGroup group;
    private ItemStack display;
    private String name;
    private AdvancementType type = AdvancementType.NORMAL;
    private ItemStack goal;

    public AdvancementBuilder() {

    }

    public AdvancementBuilder key(NamespacedKey key) {
        this.key = key;
        return this;
    }

    public AdvancementBuilder group(AdvancementGroup group) {
        this.group = group;
        return this;
    }

    public AdvancementBuilder display(ItemStack display) {
        this.display = display;
        return this;
    }

    public AdvancementBuilder name(String name) {
        this.name = name;
        return this;
    }

    public AdvancementBuilder type(AdvancementType type) {
        this.type = type;
        return this;
    }

    public AdvancementBuilder goal(ItemStack goal) {
        this.goal = goal;
        return this;
    }

    public void register() {
        Advancement adv;
        switch(type) {
            case INVENTORY -> adv = new InventoryAdvancement(key, group, display, name, goal);
            default -> adv = new Advancement(key, group, display, name);
        }
        adv.register();
    }

    public Advancement toAdvancement() {
        return new Advancement(key, group, display, name);
    }

}

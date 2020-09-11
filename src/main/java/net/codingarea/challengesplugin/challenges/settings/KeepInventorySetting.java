package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class KeepInventorySetting extends Setting {

    public KeepInventorySetting() {
        super(MenuType.SETTINGS);
        try {
            enabled = Bukkit.getWorlds().get(0).getGameRuleValue(GameRule.KEEP_INVENTORY);
        } catch (Exception ignored) { }
    }

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemBuilder(Material.ENDER_CHEST, ItemTranslation.KEEP_INVENTORY).getItem();
    }

    @Override
    public void onEnable(ChallengeEditEvent event) {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
        }

    }

    @Override
    public void onDisable(ChallengeEditEvent event) {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.KEEP_INVENTORY, false);
        }
    }
}
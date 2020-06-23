package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 02-6-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class NoTradingSetting extends Setting implements Listener {

    public NoTradingSetting() {
        menu = MenuType.SETTINGS;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.EMERALD, ItemTranslation.NO_TRADING).build();
    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (!enabled || !Challenges.timerIsStarted()) return;
        if (event.getRightClicked() instanceof Villager) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
            event.setCancelled(true);
        }

    }

}

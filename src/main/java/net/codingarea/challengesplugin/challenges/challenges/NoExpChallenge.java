package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class NoExpChallenge extends Setting implements Listener {

    public NoExpChallenge() {
        this.menu = MenuType.CHALLENGES;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.EXPERIENCE_BOTTLE, ItemTranslation.NO_XP).build();
    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @EventHandler
    public void onExp(PlayerExpChangeEvent event) {
        if (!this.enabled || !Challenges.timerIsStarted()) return;
        if (event.getAmount() < 0) return;
        event.getPlayer().damage(event.getPlayer().getHealth());

    }
}
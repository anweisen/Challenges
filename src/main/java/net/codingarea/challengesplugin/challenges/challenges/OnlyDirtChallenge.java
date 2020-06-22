package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class OnlyDirtChallenge extends Setting implements Listener {

    public OnlyDirtChallenge() {
        this.menu = MenuType.CHALLENGES;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.DIRT, ItemTranslation.ONLY_DIRT).build();
    }

    @Override
    public void onEnable(ChallengeEditEvent event) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:gamerule randomTickSpeed 0");

    }

    @Override
    public void onDisable(ChallengeEditEvent event) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:gamerule randomTickSpeed 3");

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!this.enabled || !Challenges.timerIsStarted()) return;
        Material material = event.getTo().clone().subtract(0,1,0).getBlock().getType();
        if (material != Material.DIRT && material != Material.AIR) {
            event.getPlayer().damage(event.getPlayer().getHealth());
        }

    }
}
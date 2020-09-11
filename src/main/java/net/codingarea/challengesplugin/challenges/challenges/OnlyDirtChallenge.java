package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.WorldManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class OnlyDirtChallenge extends Setting implements Listener {

    public OnlyDirtChallenge() {
        super(MenuType.CHALLENGES);
    }

    @Override
    public @NotNull ItemStack getItem() {
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
        if (WorldManager.isInExtraWorld(event.getPlayer())) return;
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        Material material = event.getTo().clone().subtract(0,1,0).getBlock().getType();
        if (material != Material.DIRT && !material.isAir() && material != Material.OBSIDIAN && material != Material.END_PORTAL && material != Material.END_PORTAL_FRAME) {
            Bukkit.broadcastMessage(Prefix.CHALLENGES + Translation.ONLY_DIRT_FAIL.get().replace("%player%", event.getPlayer().getName()).replace("%block%", Utils.getEnumName(material)));
            event.getPlayer().damage(event.getPlayer().getHealth());
        }
    }

}
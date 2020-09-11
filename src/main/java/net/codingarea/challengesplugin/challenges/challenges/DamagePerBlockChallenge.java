package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challenges.difficulty.SplitHealthSetting;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder.LeatherArmorBuilder;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen & Dominik
 * Challenges developed on 08-08-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class DamagePerBlockChallenge extends Setting implements Listener {

    public DamagePerBlockChallenge() {
        super(MenuType.CHALLENGES);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!this.enabled || !Challenges.timerIsStarted()) return;
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        if (event.getFrom().getBlockZ() == event.getTo().getBlockZ() && event.getFrom().getBlockX() == event.getTo().getBlockX()) return;
        event.getPlayer().setNoDamageTicks(0);
        event.getPlayer().damage(2);
        SplitHealthSetting.sync(event.getPlayer());
        event.getPlayer().setNoDamageTicks(0);
    }

    @Override
    public void onEnable(ChallengeEditEvent event) {

    }

    @Override
    public void onDisable(ChallengeEditEvent event) {

    }

    @Override
    public @NotNull ItemStack getItem() {
        return new LeatherArmorBuilder(Material.LEATHER_BOOTS, ItemTranslation.DAMAGE_PER_BLOCK, Color.RED).hideAttributes().build();
    }
}
